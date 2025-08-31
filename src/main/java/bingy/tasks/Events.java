package bingy.tasks;

/**
 * Represents an event task with a description, start time, and end time.
 * <p>
 * An {@link Events} task is a concrete subclass of {@link bingy.tasks.Task}
 * that includes two string fields to represent the start and end times.
 * </p>
 */
public class Events extends Task {
    private final String start;
    private final String end;

    /**
     * Creates a new {@link Events} task with the given description, start, and end times.
     *
     * @param description the description of the event
     * @param start the start time of the event
     * @param end the end time of the event
     */
    public Events(String description, String start, String end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    @Override
    protected String typeTag() {
        return "E";
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s (from: %s, to: %s)", typeTag(), getStatusIcon(),
                                                getDescription(), this.start, this.end);
    }

}
