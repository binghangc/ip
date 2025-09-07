package bingy.exceptions;

/**
 * Exception thrown when a search command is missing a keyword.
 * This ensures that users provide a valid search term rather than leaving it empty.
 */
public class EmptyKeywordException extends BingyException {

    /**
     * Constructs a new {@code EmptyKeywordException} with a default
     * error message prompting the user to provide a keyword.
     */
    public EmptyKeywordException() {
        super("Cannot search nothing: Please add a keyword you want to search");
    }
}
