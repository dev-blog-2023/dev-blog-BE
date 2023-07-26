package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class EmailVerificationTokenNotFoundException extends NotFoundException {
    public EmailVerificationTokenNotFoundException() {
        super("Email Verification Token Not Found Exception");
    }

    public EmailVerificationTokenNotFoundException(String message) {
        super(message);
    }
}
