package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.User;

@Getter @Setter
public class SignupDto {
    private String username;
    private String password;
    private String name;
    private String email;

    public User toEntity() {
        User user = new User();
        user.setUsername(this.username);
        user.setName(this.name);
        user.setEmail(this.email);

        return user;
    }
}
