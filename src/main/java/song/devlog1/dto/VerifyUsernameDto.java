package song.devlog1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class VerifyUsernameDto {
    @NotBlank
    private String username;
}
