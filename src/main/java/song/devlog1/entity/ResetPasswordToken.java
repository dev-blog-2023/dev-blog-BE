package song.devlog1.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor
public class ResetPasswordToken {
    @Id @GeneratedValue
    private Long id;

    private String username;
    private String token;
    private LocalDateTime createDateTime;
    private LocalDateTime expiryDateTime;

    @PrePersist
    private void prePersist() {
        createDateTime = LocalDateTime.now();
        expiryDateTime = createDateTime.plusMinutes(30L);
    }

    public ResetPasswordToken(String username) {
        this.username = username;
    }
}
