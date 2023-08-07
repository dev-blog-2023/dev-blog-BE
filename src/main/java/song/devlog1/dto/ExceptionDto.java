package song.devlog1.dto;

import lombok.Getter;

@Getter
public class ExceptionDto {
    private String field;
    private String message;

    public ExceptionDto(String field, String message) {
        this.field = field;
        this.message = message;
    }
}
