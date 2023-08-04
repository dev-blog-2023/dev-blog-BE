package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.Role;
import song.devlog1.entity.User;
import song.devlog1.entity.UserRole;
import song.devlog1.entity.role.RoleName;
import song.devlog1.exception.notfound.UserNotFoundException;
import song.devlog1.repository.UserJpaRepository;
import song.devlog1.repository.UserRoleJpaRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleJpaRepository userRoleRepository;
    private final UserJpaRepository userRepository;
    private final RoleService roleService;

    @Transactional
    public Long grantRole(Long userId, RoleName roleName) {
        Optional<UserRole> findUserRole = getUserRoleByUserIdAndRoleName(userId, roleName);
        if (findUserRole.isPresent()) {
            return findUserRole.get().getId();
        }
        User findUser = getUserById(userId);

        Role role = roleService.findOrCreateRole(roleName);

        UserRole userRole = new UserRole(findUser, role);

        UserRole saveUserRole = userRoleRepository.save(userRole);

        return saveUserRole.getId();
    }

    @Transactional
    public void revokeRole(Long userId, RoleName roleName) {
        Optional<UserRole> findUserRole = getUserRoleByUserIdAndRoleName(userId, roleName);
        if (findUserRole.isPresent()) {
            userRoleRepository.delete(findUserRole.get());
        }
    }

    private Optional<UserRole> getUserRoleByUserIdAndRoleName(Long userId, RoleName roleName) {
        return userRoleRepository.findByUserIdAndRoleName(userId, roleName);
    }

    private User getUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser.get();
    }
}
