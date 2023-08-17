package song.devlog1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindUsernameDto {
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
