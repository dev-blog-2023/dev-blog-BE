package song.devlog1.exception;

import org.springframework.security.core.AuthenticationException;

public class JwtProcessException extends AuthenticationException {
    public JwtProcessException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public JwtProcessException(String msg) {
        super("Jwt Process Exception");
    }
}
