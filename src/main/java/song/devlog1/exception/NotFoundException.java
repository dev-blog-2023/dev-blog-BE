package song.devlog1.exception;

public class NotFoundException extends RuntimeException {
    public NotFoundException() {
        super("Not Found Exception");
    }

    public NotFoundException(String message) {
        super(message);
    }
}
