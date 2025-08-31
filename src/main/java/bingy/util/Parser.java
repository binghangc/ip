package bingy.util;

import java.time.LocalDate;

/**
 * Provides methods to parse both user commands and stored task lines.
 * <p>
 * Used at runtime to convert raw user input into a structured {@link Parser.ParsedCommand}
 * for execution, and also to reconstruct tasks from their serialized form in storage.
 */
public class Parser {
    /**
     * A structured representation of a parsed command.
     * <p>
     * Instances are produced by {@link #parseUserCommand(String)} for runtime commands
     * and by {@link #parseStorageLine(String)} for persisted task lines.
     */
    public static class ParsedCommand {
        /**
         * All supported command kinds recognized by the parser.
         */
        public enum Type {
            LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, BYE, UNKNOWN
        }

        /**
         * Parsed command metadata and arguments. Fields may be {@code null} when
         * intentionally left unspecified so that the caller (e.g., Bingy) can
         * surface a more specific error message instead of a generic UNKNOWN.
         */
        public Type type;
        public String arg1;
        public String arg2;
        public String arg3;
        public LocalDate deadline;
        public boolean isDone;

        /**
         * Creates a command with only a {@link Type} and no arguments.
         * Useful for simple commands like LIST or BYE.
         */
        public ParsedCommand(Type type) {
            this.type = type;
        }

        /**
         * Creates a command with a primary string argument (e.g., index or description).
         *
         * @param arg1 primary argument (may be {@code null} when missing)
         */
        public ParsedCommand(Type type, String arg1) {
            this.type = type;
            this.arg1 = arg1;
        }

        /**
         * Creates a DEADLINE command with description and due date.
         *
         * @param arg1 description text (may be {@code null})
         * @param deadline ISO-8601 date for the deadline (may be {@code null})
         */
        public ParsedCommand(Type type, String arg1, LocalDate deadline) {
            this.type = type;
            this.arg1 = arg1;
            this.deadline = deadline;
        }

        /**
         * Creates a command with up to three string arguments (e.g., EVENT: desc, start, end).
         *
         * @param arg1 first argument (e.g., description)
         * @param arg2 second argument (e.g., start time)
         * @param arg3 third argument (e.g., end time)
         */
        public ParsedCommand(Type type, String arg1, String arg2, String arg3) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }

        /**
         * Creates a command that carries a completion flag (used when parsing storage lines).
         *
         * @param isDone whether the item was marked completed
         */
        public ParsedCommand(Type type, boolean isDone) {
            this.type = type;
            this.isDone = isDone;
        }

        /**
         * Creates a command with a completion flag and a primary argument.
         *
         * @param isDone completion status parsed from storage
         * @param arg1 primary argument value
         */
        public ParsedCommand(Type type, boolean isDone, String arg1) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
        }

        /**
         * Creates a DEADLINE command from storage with completion flag and due date.
         *
         * @param isDone completion status parsed from storage
         * @param arg1 description value
         * @param deadline due date value
         */
        public ParsedCommand(Type type, boolean isDone, String arg1, LocalDate deadline) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
            this.deadline = deadline;
        }

        /**
         * Creates an EVENT command from storage with completion flag.
         *
         * @param isDone completion status parsed from storage
         * @param arg1 description
         * @param arg2 start time
         * @param arg3 end time
         */
        public ParsedCommand(Type type, boolean isDone, String arg1, String arg2, String arg3) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }
    }

    /**
     * Parses a raw user input line into a {@link ParsedCommand}.
     * <p>
     * The parser avoids throwing user-facing exceptions; instead, it returns specific
     * command types with {@code null} fields when arguments are missing so the caller
     * can provide contextual error messages. Unknown commands are returned as
     * {@code Type.UNKNOWN}.
     *
     * @param input the raw user-typed line
     * @return a structured {@link ParsedCommand} describing the intent and arguments
     */
    public static ParsedCommand parseUserCommand(String input) {
        if (input == null) {
            return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
        }
        input = input.trim();

        if (input.isEmpty()) {
            return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
        }

        String lowerInput = input.toLowerCase();

        if (lowerInput.equals("list")) {
            return new ParsedCommand(ParsedCommand.Type.LIST);
        }

        if (lowerInput.equals("bye")) {
            return new ParsedCommand(ParsedCommand.Type.BYE);
        }

        String[] parts = input.split(" ", 2);
        String command = parts[0].toLowerCase();

        switch (command) {
        case "mark":
        case "unmark":
        case "delete":
            if (parts.length < 2) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }
            String indexStr = parts[1].trim();
            return new ParsedCommand(
                    command.equals("mark") ? ParsedCommand.Type.MARK :
                            command.equals("unmark") ? ParsedCommand.Type.UNMARK :
                                    ParsedCommand.Type.DELETE,
                    indexStr
            );
        case "todo":
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                return new ParsedCommand(ParsedCommand.Type.TODO, (String) null);
            }
            return new ParsedCommand(ParsedCommand.Type.TODO, parts[1].trim());
        case "deadline":
            if (parts.length < 2) {
                // no payload after "deadline"
                return new ParsedCommand(ParsedCommand.Type.DEADLINE, null, (LocalDate) null);
            }
            String[] deadlineParts = parts[1].split(" /by ", 2);
            String descPart = deadlineParts[0].trim();

            if (descPart.isEmpty()) {
                // missing description
                return new ParsedCommand(ParsedCommand.Type.DEADLINE, null, (LocalDate) null);
            }

            if (deadlineParts.length < 2 || deadlineParts[1].trim().isEmpty()) {
                // description present, but missing or empty /by portion
                return new ParsedCommand(ParsedCommand.Type.DEADLINE, descPart, (LocalDate) null);
            }
            // description and /by present; try ISO parse (yyyy-MM-dd)
            LocalDate byDate = LocalDate.parse(deadlineParts[1].trim());
            return new ParsedCommand(ParsedCommand.Type.DEADLINE, descPart, byDate);
        case "event":
            if (parts.length < 2) {
                return new ParsedCommand(ParsedCommand.Type.EVENT, (String) null, (String) null, (String) null);
            }
            String descAndTimes = parts[1].trim();

            if (descAndTimes.isEmpty()) {
                return new ParsedCommand(ParsedCommand.Type.EVENT, (String) null, (String) null, (String) null);
            }
            int fromIndex = descAndTimes.indexOf(" /from ");
            String description = null;
            String start = null;
            String end = null;

            if (fromIndex == -1) {
                // No /from provided; treat everything as description only
                description = descAndTimes.trim();
            } else {
                description = descAndTimes.substring(0, fromIndex).trim();
                int toIndex = descAndTimes.indexOf(" /to ", fromIndex + 7);

                if (toIndex == -1) {
                    // Have /from but no /to yet → missing end time
                    start = descAndTimes.substring(fromIndex + 7).trim();
                } else {
                    start = descAndTimes.substring(fromIndex + 7, toIndex).trim();
                    end = descAndTimes.substring(toIndex + 5).trim();
                }
            }
            // Normalize empty strings to null for cleaner checks downstream
            if (description != null && description.isEmpty()) {
                description = null;
            }

            if (start != null && start.isEmpty()) {
                start = null;
            }

            if (end != null && end.isEmpty()) {
                end = null;
            }

            return new ParsedCommand(ParsedCommand.Type.EVENT, description, start, end);
        default:
            return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
        }
    }

    /**
     * Parses a single line from the storage file back into a {@link ParsedCommand}
     * representing a task (TODO/DEADLINE/EVENT) with its completion state.
     *
     * @param line a serialized task line (e.g., "[D][X] finish (by: 2025-12-31)")
     * @return a {@link ParsedCommand} suitable for reconstruction, or UNKNOWN if malformed
     */
    public static ParsedCommand parseStorageLine(String line) {
        if (line == null || line.length() < 7) {
            return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
        }

        // Expected prefixes like: [T][X] , [D][ ], [E][X]
        char typeChar = line.charAt(1); // T, D, or E
        boolean isDone = line.charAt(4) == 'X';


        String rest = line.substring(7).trim();

        switch (typeChar) {
        case 'T': { // [T][ ] description
            if (rest.isEmpty()) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }
            return new ParsedCommand(ParsedCommand.Type.TODO, isDone, rest);
        }
        case 'D': { // [D][ ] description (by: when)
            int byIdx = rest.lastIndexOf("(by:");

            if (byIdx == -1) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }

            String desc = rest.substring(0, byIdx).trim();
            String whenPart = rest.substring(byIdx).trim();

            if (!whenPart.startsWith("(by:") || !whenPart.endsWith(")")) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }
            String by = whenPart.substring(4, whenPart.length() - 1).trim();

            if (by.startsWith(":")) {
                by = by.substring(1).trim();
            }

            if (desc.isEmpty() || by.isEmpty()) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }
            return new ParsedCommand(ParsedCommand.Type.DEADLINE, isDone, desc, LocalDate.parse(by));
        }
        case 'E': {
            int fromIdx = rest.lastIndexOf("(from:");

            if (fromIdx == -1 || !rest.endsWith(")")) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }

            String desc = rest.substring(0, fromIdx).trim();
            String timesPart = rest.substring(fromIdx + 6, rest.length() - 1).trim();

            if (timesPart.startsWith(":")) {
                timesPart = timesPart.substring(1).trim();
            }

            int toSep = timesPart.lastIndexOf(", to:");

            if (toSep == -1) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }

            String start = timesPart.substring(0, toSep).trim();
            String end = timesPart.substring(toSep + 5).trim();

            if (desc.isEmpty() || start.isEmpty() || end.isEmpty()) {
                return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
            }

            return new ParsedCommand(ParsedCommand.Type.EVENT, isDone, desc, start, end);
        }
        default:
            return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
        }
    }


}
