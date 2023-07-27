package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.EmailVerificationToken;

import java.util.Optional;

@Repository
public interface EmailVerificationJpaRepository extends JpaRepository<EmailVerificationToken, Long> {

    @Query("select t from EmailVerificationToken t where t.email = :email")
    Optional<EmailVerificationToken> findByEmail(@Param(value = "email") String email);

    @Query("select t from EmailVerificationToken t where t.email = :email and t.token = :token")
    Optional<EmailVerificationToken> findByEmailAndToken(@Param(value = "email") String email,
                                                         @Param(value = "token") String token);
}
