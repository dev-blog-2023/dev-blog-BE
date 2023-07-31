package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class FileEntityNotFoundException extends NotFoundException {
    public FileEntityNotFoundException() {
        super("FileEntity Not Found Exception");
    }

    public FileEntityNotFoundException(String message) {
        super(message);
    }
}
