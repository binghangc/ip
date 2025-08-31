package bingy.exceptions;

public class EmptyEventTimeException extends IllegalArgumentException {
    public EmptyEventTimeException() {
        super("Please add start and end time for your event\n Example: \"event project meeting /from Mon 2pm /to 4pm\"");
    }

}
