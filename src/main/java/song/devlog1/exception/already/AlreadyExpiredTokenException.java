package song.devlog1.exception.already;

import song.devlog1.exception.AlreadyException;

public class AlreadyExpiredTokenException extends AlreadyException {

    public AlreadyExpiredTokenException() {
        super("Already Expired Token Exception ");
    }

    public AlreadyExpiredTokenException(String message) {
        super(message);
    }
}
