package bingy.exceptions;

public class EmptyKeywordException extends IllegalArgumentException {
    public EmptyKeywordException() {
        super("Cannot search nothing: Please add a keyword you want to search");
    }
}
