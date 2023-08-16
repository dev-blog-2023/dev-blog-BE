package song.devlog1.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.User;

@Getter @Setter
public class SignupDto {
    @NotBlank
    private String username;
    @NotBlank
    private String password;
    @NotBlank
    private String name;
    @NotBlank
    private String email;

    public User toEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setName(this.name);
        user.setEmail(this.email);

        return user;
    }
}
