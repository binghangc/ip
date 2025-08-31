package bingy.tasks;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a description and a due date.
 * <p>
 * A {@link Deadline} is a concrete subclass of {@link bingy.tasks.Task}
 * that associates a textual description with a {@link java.time.LocalDate}
 * representing the due date.
 * </p>
 */
public class Deadline extends Task {
    private final LocalDate deadline;

    /**
     * Creates a new {@link Deadline} task with the specified description and due date.
     *
     * @param description the description of the deadline task
     * @param deadline the {@link java.time.LocalDate} by which the task is due
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    protected String typeTag() {
        return "D";
    }

    @Override
    public String toString() {
        return String.format("[%s][%s] %s (by: %s)", typeTag(),
                getStatusIcon(), getDescription(), deadline.format(DateTimeFormatter.ofPattern("MMM d yyyy")));
    }

    /**
     * Returns a string representation of this deadline suitable for storage.
     * <p>
     * The due date is formatted in ISO-8601 ({@code yyyy-MM-dd}) format to ensure
     * consistency when reloading tasks from persistent storage.
     * </p>
     *
     * @return the storage-ready string representation of this deadline
     */
    public String toStorageString() {
        return String.format("[%s][%s] %s (by: %s)", typeTag(),
                getStatusIcon(), getDescription(),
                deadline.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
