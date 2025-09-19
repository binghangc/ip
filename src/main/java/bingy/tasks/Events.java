package bingy.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task with a description, start time, and end time.
 * <p>
 * An {@link Events} task is a concrete subclass of {@link bingy.tasks.Task}
 * that includes two string fields to represent the start and end times.
 * </p>
 */
public class Events extends TimedTask {
    private LocalDateTime start;
    private LocalDateTime end;

    /**
     * Creates a new {@link Events} task with the given description, start, and end times.
     *
     * @param description the description of the event.
     * @param start the start time of the event.
     * @param end the end time of the event.
     */
    public Events(String description, LocalDateTime start, LocalDateTime end) {
        super(description);
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    @Override
    protected String formatWith(DateTimeFormatter formatter) {
        return String.format("[%s][%s] %s (from: %s, to: %s)", typeTag(), getStatusIcon(),
                getDescription(), start.format(formatter), end.format(formatter));
    }

    @Override
    protected String typeTag() {
        return "E";
    }

    @Override
    public boolean occursOn(LocalDate date) {
        LocalDateTime dayStart = date.atStartOfDay();
        LocalDateTime dayEnd = date.atTime(23, 59, 59, 999_999_999);
        return !end.isBefore(dayStart) && !start.isAfter(dayEnd);
    }

    @Override
    public LocalDateTime getScheduleTime(LocalDate date) {
        return start;
    }

}
