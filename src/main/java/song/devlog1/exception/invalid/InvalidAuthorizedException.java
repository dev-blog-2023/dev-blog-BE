package song.devlog1.exception.invalid;

import song.devlog1.exception.InvalidException;

public class InvalidAuthorizedException extends InvalidException {
    public InvalidAuthorizedException() {
        super("Invalid Authorized Exception");
    }

    public InvalidAuthorizedException(String message) {
        super(message);
    }
}
