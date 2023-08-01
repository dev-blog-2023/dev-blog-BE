package song.devlog1.exception.notfound;

import song.devlog1.exception.NotFoundException;

public class CommentNotFoundException extends NotFoundException {
    public CommentNotFoundException() {
        super("Comment Not Found Exception");
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}
