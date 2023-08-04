package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.SignupDto;
import song.devlog1.entity.User;
import song.devlog1.repository.UserJpaRepository;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserJpaRepository userRepository;

    @Test
    void save1() {
        SignupDto signupDto = new SignupDto();
        signupDto.setUsername("Test Username");
        signupDto.setName("Test Name");
        signupDto.setPassword("Test Password");
        signupDto.setEmail("Test Email");

        Long id = userService.saveUser(signupDto);

        User findUser = userRepository.findById(id).get();

        assertThat(findUser.getUsername()).isEqualTo(signupDto.getUsername());
    }

    @Test
    void find1() {
        Long userId = 1L;
        User findUser = userService.findUser(userId);

        assertThat(findUser.getId()).isEqualTo(userId);
    }

    @Test
    void findUsername1() {
        String name = "a";
        String email = "dkclasltmf22@naver.com";
        String findUsername = userService.findUsername(name, email);

        User findUser = userRepository.findByUsername(findUsername).get();

        assertThat(findUser.getName()).isEqualTo(name);
    }

    @Test
    void resetPassword1() {
        String username = "a";
        String name = "a";
        String email = "dkclasltmf22@naver.com";
        Long id = userService.resetPassword(username, "newPassword");
    }

}