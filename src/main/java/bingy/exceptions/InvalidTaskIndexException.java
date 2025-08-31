package bingy.exceptions;

public class InvalidTaskIndexException extends IllegalArgumentException {
    public InvalidTaskIndexException(String message) {
        super(message);
    }
}
