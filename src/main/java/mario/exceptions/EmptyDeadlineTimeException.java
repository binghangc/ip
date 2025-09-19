package mario.exceptions;

/**
 * Exception extending from {@link IllegalArgumentException} if user inputs deadline without a deadline
 */
public class EmptyDeadlineTimeException extends MarioException {
    public EmptyDeadlineTimeException() {
        super("Please add a deadline you want your task done by\n Example: \"deadline return book /by [YYYY-MM-DD]\"");
    }
}
