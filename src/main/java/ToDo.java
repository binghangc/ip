public class ToDo extends Task {
    public ToDo(String description) {
        super(description);
    }

    @Override
    protected String typeTag() {
        return "T";
    }
}