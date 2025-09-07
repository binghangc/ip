package bingy.exceptions;

public class BingyException extends Exception {
    public BingyException(String message) {
        super(message);
    }

    public BingyException() {
        super();
    }
}
