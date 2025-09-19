package bingy.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a deadline task with a description and a due date.
 * <p>
 * A {@link Deadline} is a concrete subclass of {@link bingy.tasks.Task}
 * that associates a textual description with a {@link java.time.LocalDate}
 * representing the due date.
 * </p>
 */
public class Deadline extends TimedTask {
    private final LocalDate deadline;

    /**
     * Creates a new {@link Deadline} task with the specified description and due date.
     *
     * @param description the description of the deadline task.
     * @param deadline the {@link java.time.LocalDate} by which the task is due.
     */
    public Deadline(String description, LocalDate deadline) {
        super(description);
        this.deadline = deadline;
    }

    @Override
    protected String formatWith(DateTimeFormatter formatter) {
        return String.format("[%s][%s] %s (by: %s)",
                typeTag(),
                getStatusIcon(),
                getDescription(),
                deadline.format(formatter));
    }

    @Override
    protected String typeTag() {
        return "D";
    }

    @Override
    public String toString() {
        return formatWith(DATE_ONLY_FORMATTER);
    }

    @Override
    public boolean occursOn(LocalDate date) {
        return deadline.equals(date);
    }

    @Override
    public String toStorageString() {
        return formatWith(STORAGE_DATE_FORMATTER);
    }

    @Override
    public LocalDateTime getScheduleTime(LocalDate date) {
        return LocalDateTime.of(deadline, LocalTime.of(23, 59));
    }
}
