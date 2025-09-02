package bingy.exceptions;

/**
 * Exception thrown when a user specifies a task index that does not exist
 * or is otherwise invalid. This helps prevent operations such as mark,
 * unmark, or delete on non-existent tasks.
 */
public class InvalidTaskIndexException extends IllegalArgumentException {
    /**
     * Constructs a new {@code InvalidTaskIndexException} with the specified
     * detail message indicating why the task index was invalid.
     *
     * @param message the detail message explaining the invalid index.
     */
    public InvalidTaskIndexException(String message) {
        super(message);
    }
}
