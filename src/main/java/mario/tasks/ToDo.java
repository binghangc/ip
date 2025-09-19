package mario.tasks;

/**
 * A concrete subclass that extends from {@link mario.tasks.Task}
 * <p>
 * Represents the most basic form of the Task class with only the description field.
 */
public class ToDo extends Task {

    /**
     * Initializes a {@link ToDo} task with the given description.
     *
     * @param description the textual description of the to-do item.
     */
    public ToDo(String description) {
        super(description);
    }

    @Override
    protected String typeTag() {
        return "T";
    }
}
