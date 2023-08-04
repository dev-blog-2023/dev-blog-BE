package song.devlog1.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.UserRole;
import song.devlog1.entity.role.RoleName;

import java.util.Optional;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRole, Long> {

    @EntityGraph(attributePaths = {"user", "role"})
    @Query("select ur from UserRole ur where ur.id = :id")
    Optional<UserRole> findById(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"user", "role"})
    @Query("select ur from UserRole ur where ur.user.id = :userId and ur.role.roleName = :roleName")
    Optional<UserRole> findByUserIdAndRoleName(@Param(value = "userId") Long userId,
                                               @Param(value = "roleName") RoleName roleName);

}
