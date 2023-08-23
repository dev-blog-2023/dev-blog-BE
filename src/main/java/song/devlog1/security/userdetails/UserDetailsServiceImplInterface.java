package song.devlog1.security.userdetails;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserDetailsServiceImplInterface extends UserDetailsService {
    UserDetails loadUserById(Long id);
}
