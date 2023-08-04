package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.UserRole;
import song.devlog1.entity.role.RoleName;
import song.devlog1.repository.UserRoleJpaRepository;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class UserRoleServiceTest {

    @Autowired
    UserRoleService userRoleService;
    @Autowired
    UserRoleJpaRepository userRoleRepository;

    @Test
    void grant1() {
        Long userId = 1L;
        RoleName roleName = RoleName.ROLE_ADMIN;
        Long userRoleId = userRoleService.grantRole(userId, roleName);

        UserRole findUserRole = userRoleRepository.findById(userRoleId).get();

        assertThat(findUserRole.getUser().getId()).isEqualTo(userId);
        assertThat(findUserRole.getRole().getRoleName()).isEqualTo(roleName);
    }

    @Test
    void revoke1() {
        Long userId = 1L;
        RoleName roleName = RoleName.ROLE_ADMIN;
        Long userRoleId = userRoleService.grantRole(userId, roleName);

        userRoleService.revokeRole(userId, roleName);

        assertThatThrownBy(() -> userRoleRepository.findById(userRoleId).get())
                .isInstanceOf(NoSuchElementException.class);
    }

}