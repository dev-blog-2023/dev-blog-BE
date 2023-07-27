package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class UploadFileDto {
    private String uploadFileName;
    private Long uploaded;
    private String fileName;
    private String url;

    public UploadFileDto(String originalFilename, String saveFileName) {
        this.uploadFileName = originalFilename;
        this.uploaded = 1L;
        this.fileName = saveFileName;
        this.url = "/file/downloadFile/" + saveFileName;
    }
}
