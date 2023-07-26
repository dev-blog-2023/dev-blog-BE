package song.devlog1.exception.invalid;

import song.devlog1.exception.InvalidException;

public class InvalidTokenException extends InvalidException {
    public InvalidTokenException() {
        super("Invalid Token Exception");
    }

    public InvalidTokenException(String message) {
        super(message);
    }
}
