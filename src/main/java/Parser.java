public class Parser {
    public static class ParsedCommand {
        public enum Type {
            LIST, TODO, DEADLINE, EVENT, MARK, UNMARK, DELETE, BYE, UNKNOWN
        }

        public Type type;
        public String arg1;
        public String arg2;
        public String arg3;
        public boolean isDone;

        public ParsedCommand(Type type) {
            this.type = type;
        }

        public ParsedCommand(Type type, String arg1) {
            this.type = type;
            this.arg1 = arg1;
        }

        public ParsedCommand(Type type, String arg1, String arg2) {
            this.type = type;
            this.arg1 = arg1;
            this.arg2 = arg2;
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

        public ParsedCommand(Type type, boolean isDone, String arg1, String arg2) {
            this.type = type;
            this.isDone = isDone;
            this.arg1 = arg1;
            this.arg2 = arg2;
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
                    return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
                }
                return new ParsedCommand(ParsedCommand.Type.TODO, parts[1].trim());
            case "deadline":
                if (parts.length < 2) {
                    return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
                }
                String[] deadlineParts = parts[1].split(" /by ", 2);
                if (deadlineParts.length < 2 || deadlineParts[0].trim().isEmpty() || deadlineParts[1].trim().isEmpty()) {
                    return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
                }
                return new ParsedCommand(ParsedCommand.Type.DEADLINE, deadlineParts[0].trim(), deadlineParts[1].trim());
            case "event":
                if (parts.length < 2) {
                    return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
                }
                String descAndTimes = parts[1];
                int fromIndex = descAndTimes.indexOf(" /from ");
                int toIndex = descAndTimes.indexOf(" /to ");
                if (fromIndex == -1 || toIndex == -1 || fromIndex >= toIndex) {
                    return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
                }
                String description = descAndTimes.substring(0, fromIndex).trim();
                String start = descAndTimes.substring(fromIndex + 7, toIndex).trim();
                String end = descAndTimes.substring(toIndex + 5).trim();
                if (description.isEmpty() || start.isEmpty() || end.isEmpty()) {
                    return new ParsedCommand(ParsedCommand.Type.UNKNOWN);
                }
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
                return new ParsedCommand(ParsedCommand.Type.DEADLINE, isDone, desc, by);
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
