package bingy.exceptions;

/**
 * Exception thrown when an event command is missing a start or end time.
 * This ensures that users provide both the /from and /to parameters
 * when creating an event.
 */
public class EmptyEventTimeException extends BingyException {

    /**
     * Constructs a new {@code EmptyEventTimeException} with a default
     * error message guiding the user to provide both start and end times.
     */
    public EmptyEventTimeException() {
        super("Please add start and end time for your event\n"
                + " Example: \"event project meeting /from YYYY-MM_ddTHH:mm /to 4pm\""
        );
    }

}
