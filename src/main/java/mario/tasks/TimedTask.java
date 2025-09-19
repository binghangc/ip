

package mario.tasks;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract base class for tasks that have a time component (e.g., deadlines, events).
 * <p>
 * Provides common date/time formatting utilities and requires subclasses to implement
 * custom formatting logic for their time-related fields.
 * </p>
 */
public abstract class TimedTask extends Task {
    /**
     * Formatter for displaying only the date (e.g., "Jan 1 2023").
     */
    protected static final DateTimeFormatter DATE_ONLY_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy");
    /**
     * Formatter for displaying the date and time in a user-friendly format (e.g., "Jan 1 2023 14:30").
     */
    protected static final DateTimeFormatter DEFAULT_FORMATTER = DateTimeFormatter.ofPattern("MMM d yyyy HH:mm");
    /**
     * Formatter for storing the date in ISO-8601 format (e.g., "2023-01-01").
     */
    protected static final DateTimeFormatter STORAGE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    protected static final DateTimeFormatter STORAGE_DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Constructs a TimedTask with the given description.
     *
     * @param description The description of the task.
     */
    public TimedTask(String description) {
        super(description);
    }

    /**
     * Formats the time-related fields of this task using the specified formatter.
     *
     * @param formatter The DateTimeFormatter to use for formatting.
     * @return The formatted string representation of this task.
     */
    protected abstract String formatWith(DateTimeFormatter formatter);

    /**
     * Returns a string representation of this deadline suitable for storage.
     * <p>
     * The due date is formatted in ISO-8601 ({@code yyyy-MM-dd}) format to ensure
     * consistency when reloading tasks from persistent storage.
     * </p>
     *
     * @return the storage-ready string representation of this deadline.
     */
    public String toStorageString() {
        return formatWith(STORAGE_FORMATTER);
    }

    public abstract boolean occursOn(LocalDate date);

    public abstract LocalDateTime getScheduleTime(LocalDate date);

    @Override
    public String toString() {
        return formatWith(DEFAULT_FORMATTER);
    }
}
