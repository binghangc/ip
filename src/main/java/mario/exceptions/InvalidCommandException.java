package mario.exceptions;

/**
 * Exception thrown when a user enters a command that is not recognized
 * by the application. This helps provide a clear error message to guide
 * the user toward valid commands.
 */
public class InvalidCommandException extends MarioException {

    /**
     * Constructs a new {@code InvalidCommandException} with a message
     * indicating the invalid command that was entered.
     *
     * @param msg the invalid command string provided by the user.
     */
    public InvalidCommandException(String msg) {
        super(String.format("\"%s\"? That's not a command. Stop wasting my time with "
                + "your gibberish or I'll just have to deal with you myself.", msg));
    }
}
