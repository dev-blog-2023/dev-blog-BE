package song.devlog1.exception.already;

import song.devlog1.exception.AlreadyException;

public class AlreadyExistsEmailException extends AlreadyException {
    public AlreadyExistsEmailException() {
        super("Already Exists Email Exception");
    }

    public AlreadyExistsEmailException(String message) {
        super(message);
    }
}
