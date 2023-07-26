package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.Role;
import song.devlog1.entity.role.RoleName;

import java.util.Optional;

@Repository
public interface RoleJpaRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.roleName = :roleName")
    Optional<Role> findByRoleName(@Param(value = "roleName") RoleName roleName);
}
