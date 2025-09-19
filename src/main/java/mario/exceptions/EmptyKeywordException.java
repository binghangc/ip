package mario.exceptions;

/**
 * Exception thrown when a search command is missing a keyword.
 * This ensures that users provide a valid search term rather than leaving it empty.
 */
public class EmptyKeywordException extends MarioException {

    /**
     * Constructs a new {@code EmptyKeywordException} with a default
     * error message prompting the user to provide a keyword.
     */
    public EmptyKeywordException() {
        super("I thought you were smart enough to figure this out. Give me a keyword, you buffoon!");
    }
}
