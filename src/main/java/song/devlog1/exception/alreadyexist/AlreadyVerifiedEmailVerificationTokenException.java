package song.devlog1.exception.alreadyexist;

import song.devlog1.exception.AlreadyException;

public class AlreadyVerifiedEmailVerificationTokenException extends AlreadyException {
    public AlreadyVerifiedEmailVerificationTokenException() {
        super("Already Verified Email Verification Token Exception");
    }

    public AlreadyVerifiedEmailVerificationTokenException(String message) {
        super(message);
    }
}
