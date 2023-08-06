package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.dto.SignupDto;
import song.devlog1.entity.User;
import song.devlog1.exception.already.AlreadyExistsUsernameException;
import song.devlog1.exception.invalid.InvalidRequestParameterException;
import song.devlog1.exception.notfound.UserNotFoundException;
import song.devlog1.repository.UserJpaRepository;
import song.devlog1.security.userdetails.UserDetailsImpl;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserJpaRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public Long saveUser(SignupDto signupDto) {
        User user = signupDto.toEntity();
        user.setPassword(passwordEncoder.encode(signupDto.getPassword()));

        User saveUser = userRepository.save(user);
        return saveUser.getId();
    }

    @Transactional
    public User findUser(Long userId) {
        User findUser = getUserById(userId);

        return findUser;
    }

    @Transactional
    public String findUsername(String name, String email) {
        Optional<User> findUser = userRepository.findByNameAndEmail(name, email);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser.get().getName();
    }

    @Transactional
    public String findPassword(String username, String name, String email) {
        Optional<User> findUser = userRepository.findByUsernameAndNameAndEmail(username, name, email);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return findUser.get().getUsername();
    }

    @Transactional
    public Long resetPassword(String username, String newPassword) {
        User findUser = getUserByUsername(username);

        if (passwordEncoder.matches(newPassword, findUser.getPassword())) {
            throw new InvalidRequestParameterException("새로운 비밀번호를 다시 입력해주세요.");
        }

        findUser.setPassword(passwordEncoder.encode(newPassword));

        User saveUser = userRepository.save(findUser);
        return saveUser.getId();
    }

    @Transactional
    public Long editEmail(Long userId, String newEmail) {
        User findUser = getUserById(userId);
        findUser.setEmail(newEmail);

        User saveUser = userRepository.save(findUser);

        updateSecurityContext(saveUser);

        return saveUser.getId();
    }

    @Transactional
    public Long editPassword(Long userId, String originalPassword, String newPassword) {
        User findUser = getUserById(userId);

        if (!passwordEncoder.matches(originalPassword, findUser.getPassword())) {
            throw new InvalidRequestParameterException("기존 비밀번호를 다시 입력해주세요.");
        }
        if (passwordEncoder.matches(newPassword, findUser.getPassword())) {
            throw new InvalidRequestParameterException("새로운 비밀번호를 다시 입력해주세요.");
        }

        findUser.setPassword(passwordEncoder.encode(newPassword));

        User saveUser = userRepository.save(findUser);

        updateSecurityContext(saveUser);

        return saveUser.getId();
    }

    @Transactional
    public Long editUsername(Long userId, String newUsername) {
        validUsername(newUsername);

        User findUser = getUserById(userId);
        findUser.setUsername(newUsername);

        User saveUser = userRepository.save(findUser);

        updateSecurityContext(saveUser);

        return saveUser.getId();
    }

    @Transactional
    public Long editName(Long userId, String newName) {
        User findUser = getUserById(userId);
        findUser.setName(newName);

        User saveUser = userRepository.save(findUser);

        updateSecurityContext(saveUser);

        return saveUser.getId();
    }

    @Transactional
    public void deleteUser(Long userId) {
        User findUser = getUserById(userId);

        SecurityContextHolder.clearContext();

        userRepository.delete(findUser);
    }

    @Transactional
    public void validUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isPresent()) {
            throw new AlreadyExistsUsernameException("이미 존재하는 Username 입니다.");
        }
    }

    private User getUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser.get();
    }

    private User getUserByUsername(String username) {
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser.get();
    }

    public void updateSecurityContext(User user) {
        UserDetailsImpl userDetails = new UserDetailsImpl(user);
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authentication);
    }
}
