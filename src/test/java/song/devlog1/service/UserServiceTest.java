package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@RequiredArgsConstructor
class UserServiceTest {

    @Autowired
    UserService userService;

    @Test
    void find1() {
        String username = userService.findUsername("a", "dkclasltmf22@naver.com");

        log.info("username = {}", username);
        Assertions.assertThat(username).isEqualTo("a");
    }

}