package song.devlog1.exception;

public class InvalidException extends RuntimeException {
    public InvalidException() {
        super("Invalid Exception");
    }

    public InvalidException(String message) {
        super(message);
    }
}
