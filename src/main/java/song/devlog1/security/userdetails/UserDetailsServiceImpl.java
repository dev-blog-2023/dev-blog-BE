package song.devlog1.security.userdetails;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.User;
import song.devlog1.repository.UserJpaRepository;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsServiceImplInterface {

    private final UserJpaRepository userRepository;
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> findUser = userRepository.findByUsername(username);
        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자(username)를 찾을 수 없습니다.");
        }
        User user = findUser.get();

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
                user.getRoleList(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled());
    }

    @Override
    @Transactional
    public UserDetails loadUserById(Long id) {
        Optional<User> findUser = userRepository.findById(id);
        if (findUser.isEmpty()) {
            throw new UsernameNotFoundException("사용자(id)를 찾을 수 없습니다.");
        }
        User user = findUser.get();

        return new UserDetailsImpl(user.getId(), user.getUsername(), user.getPassword(), user.getName(), user.getEmail(),
                user.getRoleList(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled());
    }
}
