package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FileUrlDto {
    private String url;

    public FileUrlDto(String url) {
        this.url = url;
    }
}
