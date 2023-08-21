package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.ResetPasswordToken;
import song.devlog1.repository.ResetPasswordTokenJpaRepository;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class ResetPasswordTokenServiceTest {

    @Autowired
    ResetPasswordTokenService resetPasswordTokenService;
    @Autowired
    ResetPasswordTokenJpaRepository resetPasswordTokenRepository;

    @Test
    void save1() {
        String username = "userA";

        String token = resetPasswordTokenService.saveResetPasswordToken(username);

        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.findByToken(token).get();

        assertThat(resetPasswordToken.getUsername()).isEqualTo(username);
    }

    @Test
    void verify1() {
        String username = "userA";

        String token = resetPasswordTokenService.saveResetPasswordToken(username);

        String verifiedUsername = resetPasswordTokenService.verifyResetPasswordToken(token);

        assertThat(verifiedUsername).isEqualTo(username);
    }

}