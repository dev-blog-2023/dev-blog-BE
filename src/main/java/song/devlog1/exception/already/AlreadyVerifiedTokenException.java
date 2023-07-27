package song.devlog1.exception.already;

import song.devlog1.exception.AlreadyException;

public class AlreadyVerifiedTokenException extends AlreadyException {
    public AlreadyVerifiedTokenException() {
        super("Already Verified Token Exception");
    }

    public AlreadyVerifiedTokenException(String message) {
        super(message);
    }
}
