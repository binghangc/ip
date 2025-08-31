package bingy.tasks;

/**
 * Represents an abstract task in the Bingy application.
 * <p>
 * Encapsulates common fields and behavior for all task types,
 * including a description, completion status, and formatted string output.
 * Concrete subclasses such as {@link ToDo}, {@link Deadline}, and {@link Events}
 * provide specific implementations of {@link #typeTag()}.
 */
public abstract class Task {
    private final String description;
    private boolean isDone;

    /**
     * Creates a new task with the given description.
     * Tasks are initialized as not done.
     *
     * @param description the textual description of the task
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Marks this task as completed by setting its status to done.
     */
    public void markDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed by resetting its status to undone.
     */
    public void markUndone() {
        this.isDone = false;
    }

    /**
     * Returns the textual description of this task.
     *
     * @return the description of the task
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a status icon representing whether the task is done.
     * "X" if completed, otherwise a blank space.
     *
     * @return a string representing the task's completion status
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Returns a type tag that identifies the concrete subclass of this task.
     * Implemented by subclasses such as {@link ToDo}, {@link Deadline}, and {@link Events}.
     *
     * @return the type tag string for this task
     */
    protected abstract String typeTag();

    /**
     * Returns a formatted string representation of this task,
     * including its type tag, status icon, and description.
     *
     * @return a human-readable string representing the task
     */
    @Override
    public String toString() {
        return String.format("[%s][%s] %s", typeTag(), getStatusIcon(), getDescription());
    }
}
