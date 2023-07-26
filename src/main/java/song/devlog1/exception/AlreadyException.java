package song.devlog1.exception;

public class AlreadyException extends RuntimeException {
    public AlreadyException() {
        super("Already Exist Exception");
    }

    public AlreadyException(String message) {
        super(message);
    }
}
