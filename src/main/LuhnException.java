package src.main;

public class LuhnException extends RuntimeException {
    public LuhnException() {
        super();
    }

    public LuhnException(String message) {
        super(message);
    }

    public LuhnException(String message, Throwable cause) {
        super(message, cause);
    }
}