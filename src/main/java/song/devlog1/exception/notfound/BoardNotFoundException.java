package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class BoardNotFoundException extends NotFoundException {
    public BoardNotFoundException() {
        super("Board Not Found Exception");
    }

    public BoardNotFoundException(String message) {
        super(message);
    }
}
