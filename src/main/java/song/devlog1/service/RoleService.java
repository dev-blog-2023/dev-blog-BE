package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.entity.Role;
import song.devlog1.entity.role.RoleName;
import song.devlog1.repository.RoleJpaRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleJpaRepository roleRepository;

    @Transactional
    public Role findOrCreateRole(RoleName roleName) {
        Optional<Role> findRole = roleRepository.findByRoleName(roleName);
        if (findRole.isPresent()) {
            return findRole.get();
        }

        Role role = new Role();
        role.setRoleName(roleName);

        Role saveRole = roleRepository.save(role);
        return saveRole;
    }
}
