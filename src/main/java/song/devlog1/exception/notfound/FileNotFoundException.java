package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class FileNotFoundException extends NotFoundException {
    public FileNotFoundException() {
        super("File Not Found Exception");
    }

    public FileNotFoundException(String message) {
        super(message);
    }
}
