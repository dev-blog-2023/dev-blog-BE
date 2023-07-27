package song.devlog1.exception.already;

import song.devlog1.exception.AlreadyException;

public class AlreadyExistsUsernameException extends AlreadyException {
    public AlreadyExistsUsernameException() {
        super("Already Exist Username Exception");
    }

    public AlreadyExistsUsernameException(String message) {
        super(message);
    }
}
