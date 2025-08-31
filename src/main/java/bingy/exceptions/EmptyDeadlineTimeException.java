package bingy.exceptions;

public class EmptyDeadlineTimeException extends IllegalArgumentException {
    public EmptyDeadlineTimeException() {
        super("Please add a deadline you want your task done by\n Example: \"deadline return book /by [YYYY-MM-DD]\"");
    }
}
