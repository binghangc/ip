package bingy.exceptions;

public class EmptyTaskException extends IllegalArgumentException {
    public EmptyTaskException(String taskType) {
        super(String.format("You can't add an empty %s item. Try again.", taskType));
    }
}
