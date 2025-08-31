package bingy.tasks;

public class Events extends Task {
    private final String start;
    private final String end;

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
