package song.devlog1.exception.alreadyexist;

import song.devlog1.exception.AlreadyException;

public class AlreadyExpiredEmailVerificationToken extends AlreadyException {

    public AlreadyExpiredEmailVerificationToken() {
        super("Already Expired Email Verification Token Exception ");
    }

    public AlreadyExpiredEmailVerificationToken(String message) {
        super(message);
    }
}
