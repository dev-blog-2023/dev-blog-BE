package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.Role;
import song.devlog1.entity.role.RoleName;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class RoleServiceTest {

    @Autowired
    RoleService roleService;

    @Test
    void create1() {
        Role roleAdmin = roleService.findOrCreateRole(RoleName.ROLE_ADMIN);

        assertThat(roleAdmin.getRoleName()).isEqualTo(RoleName.ROLE_ADMIN);
    }

}