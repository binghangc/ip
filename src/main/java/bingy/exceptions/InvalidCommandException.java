package bingy.exceptions;

public class InvalidCommandException extends IllegalArgumentException {
    public InvalidCommandException(String msg) {
        super(String.format("Huh?? Did you say \"%s\"? I don't know that", msg));
    }
}
