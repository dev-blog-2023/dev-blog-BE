package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailException;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class EmailServiceTest {

    @Autowired
    EmailService emailService;

    @Test
    void test1() {
        emailService.sendMail("dkclasltmf@naver.com", "Test Email", "Test");

        assertThatThrownBy(() -> emailService.sendMail("d", "Test Email", "Test"))
                .isInstanceOf(MailException.class);
    }
}