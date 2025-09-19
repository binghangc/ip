package mario.exceptions;

/**
 * Exception class representing errors specific to the Bingy application.
 */
public class MarioException extends Exception {
    /**
     * Constructs a new BingyException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public MarioException(String message) {
        super(message);
    }

    /**
     * Constructs a new BingyException with no detail message.
     */
    public MarioException() {
        super();
    }
}
