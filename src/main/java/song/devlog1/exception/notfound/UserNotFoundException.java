package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException() {
        super("User Not Found Exception");
    }

    public UserNotFoundException(String message) {
        super(message);
    }
}
