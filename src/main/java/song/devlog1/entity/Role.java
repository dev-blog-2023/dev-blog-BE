package song.devlog1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import song.devlog1.entity.role.RoleName;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Role {
    @Id @GeneratedValue
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName roleName;

    public Role(RoleName roleName) {
        this.roleName = roleName;
    }
}
