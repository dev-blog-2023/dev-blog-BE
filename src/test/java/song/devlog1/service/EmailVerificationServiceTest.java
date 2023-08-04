package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.EmailVerificationToken;
import song.devlog1.exception.invalid.InvalidTokenException;
import song.devlog1.exception.notfound.EmailVerificationTokenNotFoundException;
import song.devlog1.repository.EmailVerificationJpaRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class EmailVerificationServiceTest {

    @Autowired
    EmailVerificationService emailVerificationService;

    @Autowired
    EmailVerificationJpaRepository emailVerificationRepository;

    @Test
    void create1() {
        String token = emailVerificationService.createEmailVerificationToken("dkclasltmf@naver.com");

        EmailVerificationToken emailVerificationToken = emailVerificationRepository.findByEmail("dkclasltmf@naver.com").get();

        assertThat(emailVerificationToken.getToken()).isEqualTo(token);
    }

    @Test
    void verify1() {
        String token = emailVerificationService.createEmailVerificationToken("dkclasltmf@naver.com");

        Long id = emailVerificationService.verifyEmailVerificationToken("dkclasltmf@naver.com", token);

        EmailVerificationToken findToken = emailVerificationRepository.findById(id).get();

        assertThat(findToken.isVerified()).isTrue();
    }

    @Test
    void verify2() {
        String token = emailVerificationService.createEmailVerificationToken("dkclasltmf@naver.com");

        assertThatThrownBy(() -> emailVerificationService.verifyEmailVerificationToken(
                "dkclasltmf@naver.com", token + 1))
                .isInstanceOf(EmailVerificationTokenNotFoundException.class);
    }
}