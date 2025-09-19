package bingy.exceptions;

/**
 * Exception class representing errors specific to the Bingy application.
 */
public class BingyException extends Exception {
    /**
     * Constructs a new BingyException with the specified detail message.
     *
     * @param message the detail message explaining the reason for the exception
     */
    public BingyException(String message) {
        super(message);
    }

    /**
     * Constructs a new BingyException with no detail message.
     */
    public BingyException() {
        super();
    }
}
