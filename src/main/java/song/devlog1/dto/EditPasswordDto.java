package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EditPasswordDto {
    private String originalPassword;
    private String newPassword;
}
