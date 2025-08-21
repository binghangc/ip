public abstract class Task {
    private final String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public void markDone() {
        this.isDone = true;
    }

    public void markUndone() {
        this.isDone = false;
    }

    public String getDescription() {
        return this.description;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    protected abstract String typeTag();

    @Override
    public String toString() {
        return String.format("[%s][%s] %s", typeTag(), getStatusIcon(), getDescription());
    }
}
