import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class Deadline extends Task {
    private final LocalDate deadline;

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

    public String toStorageString() {
        return String.format("[%s][%s] %s (by: %s)", typeTag(),
                getStatusIcon(), getDescription(),
                deadline.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }
}
