package bingy.util;

import java.time.LocalDate;


public class Parser {
    public static class ParsedCommand {
        public enum Type {
            LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, BYE, UNKNOWN, FIND
        }

        public Type type;
        public String arg1;
        public String arg2;
        public String arg3;
        public LocalDate deadline;
        public boolean isDone;

        public ParsedCommand(Type type) {
            this.type = type;
        }

        public ParsedCommand(Type type, String arg1) {
            this.type = type;
            this.arg1 = arg1;
        }

        public ParsedCommand(Type type, String arg1, LocalDate deadline) {
            this.type = type;
            this.arg1 = arg1;
            this.deadline = deadline;
        }

        public ParsedCommand(Type type, String arg1, String arg2, String arg3) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }

        public ParsedCommand(Type type, boolean isDone) {
            this.type = type;
            this.isDone = isDone;
        }

        public ParsedCommand(Type type, boolean isDone, String arg1) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
        }

        public ParsedCommand(Type type, boolean isDone, String arg1, LocalDate deadline) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
            this.deadline = deadline;
        }

        public ParsedCommand(Type type, boolean isDone, String arg1, String arg2, String arg3) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
            this.arg2 = arg2;
            this.arg3 = arg3;
        }
    }

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
        case "find":
            if (parts.length < 2 || parts[1].trim().isEmpty()) {
                return new ParsedCommand(ParsedCommand.Type.FIND, (String) null);
            }
            return new ParsedCommand(ParsedCommand.Type.FIND, parts[1].trim());
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
            // Gracefully return EVENT with nulls so BingyBot can throw specific errors
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
            if (description != null && description.isEmpty()) description = null;
            if (start != null && start.isEmpty()) start = null;
            if (end != null && end.isEmpty()) end = null;
            return new ParsedCommand(ParsedCommand.Type.EVENT, description, start, end);
        default:
            return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
        }
    }

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
