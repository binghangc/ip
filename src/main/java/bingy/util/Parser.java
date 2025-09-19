package bingy.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import bingy.commands.ByeCommand;
import bingy.commands.Command;
import bingy.commands.DeadlineCommand;
import bingy.commands.DeleteCommand;
import bingy.commands.EventCommand;
import bingy.commands.ListCommand;
import bingy.commands.MarkCommand;
import bingy.commands.TodoCommand;
import bingy.commands.UnmarkCommand;
import bingy.commands.ViewCommand;
import bingy.exceptions.BingyException;
import bingy.exceptions.EmptyDeadlineTimeException;
import bingy.exceptions.EmptyEventTimeException;
import bingy.exceptions.EmptyKeywordException;
import bingy.exceptions.EmptyTaskException;
import bingy.exceptions.InvalidCommandException;
import bingy.exceptions.InvalidTaskIndexException;
import bingy.tasks.Deadline;
import bingy.tasks.Events;
import bingy.tasks.Task;
import bingy.tasks.ToDo;


/**
 * Provides methods to parse both user commands and stored task lines.
 * <p>
 * Used at runtime to convert raw user input into a structured {@link Command}
 * for execution, and also to reconstruct tasks from their serialized form in storage.
 */
public class Parser {
    /**
     * Parses a raw user input into a concrete {@link bingy.commands.Command}.
     */
    public static Command parse(String input) throws BingyException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("");
        }
        String trimmed = input.trim();
        String lower = trimmed.toLowerCase();

        // Single-word commands
        if (lower.equals("list")) {
            return new ListCommand();
        }
        if (lower.equals("bye") || lower.equals("exit")) {
            return new ByeCommand();
        }

        String[] parts = trimmed.split(" ", 2);
        String cmd = parts[0].toLowerCase();
        String rest = parts.length > 1 ? parts[1].trim() : "";

        switch (cmd) {
        case "todo": {
            // allow empty to trigger proper EmptyTaskException in TodoCommand
            return new TodoCommand(rest);
        }
        case "deadline": {
            if (rest.isEmpty()) {
                // no payload at all
                throw new EmptyTaskException("deadline");
            }
            String[] dlParts = rest.split(" /by ", 2);
            String desc = dlParts[0].trim();
            if (desc.isEmpty()) {
                throw new EmptyTaskException("deadline");
            }
            if (dlParts.length < 2 || dlParts[1].trim().isEmpty()) {
                // missing /by time
                throw new EmptyDeadlineTimeException();
            }
            LocalDate by;
            try {
                by = LocalDate.parse(dlParts[1].trim()); // expect ISO yyyy-MM-dd
            } catch (DateTimeParseException e) {
                throw new BingyException("Use ISO date for /by, e.g. 2025-09-12");
            }
            return new DeadlineCommand(desc, by);
        }
        case "event": {
            if (rest.isEmpty()) {
                throw new EmptyTaskException("event");
            }
            int fromIdx = rest.indexOf(" /from ");
            if (fromIdx == -1) {
                throw new EmptyEventTimeException();
            }
            String desc = rest.substring(0, fromIdx).trim();
            String afterFrom = rest.substring(fromIdx + 7).trim();

            int toIdx = afterFrom.indexOf(" /to ");

            if (toIdx == -1) {
                throw new EmptyEventTimeException();
            }
            String fromStr = afterFrom.substring(0, toIdx).trim();
            String toStr = afterFrom.substring(toIdx + 5).trim();

            if (desc.isEmpty()) {
                throw new EmptyTaskException("event");
            }

            if (fromStr.isEmpty() || toStr.isEmpty()) {
                throw new EmptyEventTimeException();
            }

            LocalDateTime from;
            LocalDateTime to;
            try {
                from = LocalDateTime.parse(fromStr.trim());
                to = LocalDateTime.parse(toStr.trim());
            } catch (DateTimeParseException e) {
                throw new BingyException("Use ISO date-time format for /from and /to, e.g. 2025-09-12T14:30");
            }

            return new EventCommand(desc, from, to);
        }
        case "mark": {
            int idx = parseIndexOrThrow(rest);
            return new MarkCommand(idx);
        }
        case "unmark": {
            int idx = parseIndexOrThrow(rest);
            return new UnmarkCommand(idx);
        }
        case "delete": {
            int idx = parseIndexOrThrow(rest);
            return new DeleteCommand(idx);
        }
        case "find": {
            if (rest.isEmpty()) {
                throw new EmptyKeywordException();
            }
            // If you have a FindCommand class, return it here. Otherwise, signal invalid for now.
            throw new InvalidCommandException("find");
        }
        case "view": {
            LocalDate date;
            if (rest.isEmpty()) {
                date = LocalDate.now();
            } else {
                try {
                    date = LocalDate.parse(rest);
                } catch (DateTimeParseException e) {
                    throw new BingyException("Use ISO date for view, e.g. view 2025-09-20");
                }
            }
            return new ViewCommand(date);
        }
        default:
            throw new InvalidCommandException(cmd);
        }
    }

    private static int parseIndexOrThrow(String rest) throws BingyException {
        if (rest == null || rest.isBlank()) {
            throw new InvalidTaskIndexException("Give me a task number, e.g., 2");
        }
        try {
            return Integer.parseInt(rest.trim());
        } catch (NumberFormatException e) {
            throw new InvalidTaskIndexException("That’s not a number. Try something like: mark 2");
        }
    }

    /**
     * Parses raw storage line into a {@link Task}
     *
     * @param line the raw storage input
     * @return a reconstructed {@link Task} object or null if invalid
     */
    public static Task parseStorageLine(String line) {
        if (line == null || line.length() < 7) {
            return null;
        }

        // Expected prefixes like: [T][X] , [D][ ], [E][X]
        char typeChar = line.charAt(1); // T, D, or E
        boolean isDone = line.charAt(4) == 'X';

        String rest = line.substring(7).trim();

        Task task = null;
        switch (typeChar) {
        case 'T': { // [T][ ] description
            if (rest.isEmpty()) {
                return null;
            }
            task = new ToDo(rest);
            break;
        }
        case 'D': { // [D][ ] description (by: when)
            int byIdx = rest.lastIndexOf("(by:");
            if (byIdx == -1) {
                return null;
            }
            String desc = rest.substring(0, byIdx).trim();
            String whenPart = rest.substring(byIdx).trim();

            if (!whenPart.startsWith("(by:") || !whenPart.endsWith(")")) {
                return null;
            }
            String by = whenPart.substring(4, whenPart.length() - 1).trim();

            if (by.startsWith(":")) {
                by = by.substring(1).trim();
            }

            if (desc.isEmpty() || by.isEmpty()) {
                return null;
            }
            task = new Deadline(desc, LocalDate.parse(by));
            break;
        }
        case 'E': { // [E][ ] description (from: START, to: END)
            int fromIdx = rest.lastIndexOf("(from:");
            if (fromIdx == -1 || !rest.endsWith(")")) {
                return null;
            }

            String desc = rest.substring(0, fromIdx).trim();
            String timesPart = rest.substring(fromIdx + 6, rest.length() - 1).trim();

            if (timesPart.startsWith(":")) {
                timesPart = timesPart.substring(1).trim();
            }

            int toSep = timesPart.lastIndexOf(", to:");
            if (toSep == -1) {
                return null;
            }

            String startStr = timesPart.substring(0, toSep).trim();
            String endStr = timesPart.substring(toSep + 5).trim();

            if (desc.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
                return null;
            }

            LocalDateTime start;
            try {
                start = LocalDateTime.parse(startStr);
            } catch (DateTimeParseException ex) {
                // Fallback: allow date-only and default to start of day
                start = LocalDate.parse(startStr).atStartOfDay();
            }

            LocalDateTime end;
            try {
                end = LocalDateTime.parse(endStr);
            } catch (DateTimeParseException ex) {
                // Fallback: allow date-only and default to end of day
                end = LocalDate.parse(endStr).atTime(23, 59);
            }
            task = new Events(desc, start, end);
            break;
        }
        default:
            return null;
        }

        if (isDone && task != null) {
            task.markDone();
        }
        return task;
    }


}
