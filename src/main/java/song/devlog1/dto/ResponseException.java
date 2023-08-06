package song.devlog1.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
public class ResponseException {
    private Integer status;
    private String message;
    private Map<String, String> messages;
    private LocalDateTime timestamp = LocalDateTime.now();
    private String path;

    public ResponseException(HttpStatus status, String message, String path) {
        this.status = status.value();
        this.message = message;
        this.path = path;
    }

    public ResponseException(HttpStatus status, Map<String, String> messages, String path) {
        this.status = status.value();
        this.messages = messages;
        this.path = path;
    }
}
