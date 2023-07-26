package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class ResetPasswordTokenNotFoundException extends NotFoundException {
    public ResetPasswordTokenNotFoundException() {
        super("Reset Password Token Not Found Exception");
    }

    public ResetPasswordTokenNotFoundException(String message) {
        super(message);
    }
}
