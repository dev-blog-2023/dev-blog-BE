package song.devlog1.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ResponseException {
    private Integer status;
    private List<ExceptionDto> messages = new ArrayList<>();
    private LocalDateTime timestamp = LocalDateTime.now();
    private String path;

    public ResponseException(HttpStatus status, List<ExceptionDto> messages, String path) {
        this.status = status.value();
        this.messages = messages;
        this.path = path;
    }
}
