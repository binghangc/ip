package bingy.exceptions;

/**
 * Exception thrown when a user attempts to create a task without
 * providing the required description. This prevents empty tasks
 * such as "todo", "deadline", or "event" without details.
 */
public class EmptyTaskException extends IllegalArgumentException {
    /**
     * Constructs a new {@code EmptyTaskException} with an error
     * message that indicates the type of task cannot be empty.
     *
     * @param taskType the type of task that was left empty.
     */
    public EmptyTaskException(String taskType) {
        super(String.format("You can't add an empty %s item. Try again.", taskType));
    }
}
