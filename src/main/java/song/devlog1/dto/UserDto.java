package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.User;

@Getter @Setter
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private String name;

    public UserDto(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.name = user.getName();
    }
}
