package song.devlog1.exception.invalid;

import song.devlog1.exception.InvalidException;

public class InvalidRequestParameterException extends InvalidException {
    public InvalidRequestParameterException() {
        super("Invalid Request Parameter Exception");
    }

    public InvalidRequestParameterException(String message) {
        super(message);
    }
}
