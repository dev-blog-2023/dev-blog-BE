package song.devlog1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FindPasswordDto {
    @NotBlank
    private String username;
    @NotBlank
    private String name;
    @NotBlank
    private String email;
}
