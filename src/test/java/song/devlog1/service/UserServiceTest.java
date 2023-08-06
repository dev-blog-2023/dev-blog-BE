package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.SignupDto;
import song.devlog1.entity.User;
import song.devlog1.exception.already.AlreadyExistsUsernameException;
import song.devlog1.exception.invalid.InvalidRequestParameterException;
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
    @Autowired
    PasswordEncoder passwordEncoder;

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
        String newPassword = "newPassword";
        Long id = userService.resetPassword(username, newPassword);

        User findUser = userRepository.findById(id).get();

        assertThat(passwordEncoder.matches(newPassword, findUser.getPassword())).isTrue();
    }

    @Test
    void editEmail1() {
        Long userId = 1L;
        String newEmail = "dkclasltmf@naver.com";

        Long id = userService.editEmail(userId, newEmail);

        User findUser = userRepository.findById(id).get();

        assertThat(findUser.getEmail()).isEqualTo(newEmail);
    }

    @Test
    void editPassword1() {
        Long userId = 1L;
        String origPassword = "a";
        String newPassword = "newPassword";
        Long id = userService.editPassword(userId, origPassword, newPassword);

        User findUser = userRepository.findById(id).get();

        assertThat(passwordEncoder.matches(newPassword, findUser.getPassword())).isTrue();
    }

    @Test
    void editPassword2() {
        Long userId = 1L;
        String origPassword = "a";
        String newSamePassword = "a";
        String failOrigPassword = "abc";
        String newPassword = "newPassword";

        assertThatThrownBy(() -> userService.editPassword(userId, origPassword, newSamePassword))
                .isInstanceOf(InvalidRequestParameterException.class);

        assertThatThrownBy(() -> userService.editPassword(userId, failOrigPassword, newPassword))
                .isInstanceOf(InvalidRequestParameterException.class);
    }

    @Test
    void editUsername1() {
        Long userId = 1L;
        String newUsername = "newUsername";

        Long id = userService.editUsername(userId, newUsername);

        User findUser = userRepository.findById(id).get();

        assertThat(findUser.getUsername()).isEqualTo(newUsername);
    }

    @Test
    void editUsername2() {
        Long userId = 1L;
        String saveUsername = "a";

        assertThatThrownBy(() -> userService.editUsername(userId, saveUsername))
                .isInstanceOf(AlreadyExistsUsernameException.class);
    }

    @Test
    void validUsername1() {
        String alreadyUsername = "a";
        assertThatThrownBy(() -> userService.validUsername(alreadyUsername))
                .isInstanceOf(AlreadyExistsUsernameException.class);
    }

}