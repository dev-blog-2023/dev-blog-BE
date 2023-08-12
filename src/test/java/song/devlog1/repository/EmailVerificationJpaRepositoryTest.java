package song.devlog1.repository;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.EmailVerificationToken;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class EmailVerificationJpaRepositoryTest {

    @Autowired
    EmailVerificationJpaRepository emailVerificationRepository;

    @Test
    void save1() {
        EmailVerificationToken emailVerificationToken = new EmailVerificationToken();
        emailVerificationToken.setEmail("Test email");
        emailVerificationToken.setVerified(false);
        emailVerificationToken.setToken("test");

        EmailVerificationToken saveToken = emailVerificationRepository.save(emailVerificationToken);

        EmailVerificationToken findToken = emailVerificationRepository.findById(saveToken.getId()).get();

        assertThat(findToken.getEmail()).isEqualTo(emailVerificationToken.getEmail());
    }

}