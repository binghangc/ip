public class EmptyDeadlineTime extends IllegalArgumentException {
    public EmptyDeadlineTime() {
        super("Please add a deadline you want your task done by\n Example: \"deadline return book /by Sunday\"");
    }
}
