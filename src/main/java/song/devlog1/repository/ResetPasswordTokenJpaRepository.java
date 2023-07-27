package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.ResetPasswordToken;

import java.util.Optional;

@Repository
public interface ResetPasswordTokenJpaRepository extends JpaRepository<ResetPasswordToken, Long> {

    @Query("select t from ResetPasswordToken t where t.token = :token")
    Optional<ResetPasswordToken> findByToken(@Param(value = "token") String token);

    @Query("select t from ResetPasswordToken t where t.username = :username")
    Optional<ResetPasswordToken> findByUsername(@Param(value = "username") String username);
}
