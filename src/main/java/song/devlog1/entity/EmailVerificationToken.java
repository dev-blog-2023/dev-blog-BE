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
public class EmailVerificationToken {
    @Id @GeneratedValue
    private Long id;

    private String email;
    private String token;
    private boolean isVerified;
    private LocalDateTime createTime;
    private LocalDateTime expiryTime;

    @PrePersist
    private void prePersist() {
        createTime = LocalDateTime.now();
        expiryTime = createTime.plusMinutes(30L);
    }

    public EmailVerificationToken(String email) {
        this.email = email;
    }
}
