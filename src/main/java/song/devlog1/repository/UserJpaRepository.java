package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.User;

import java.util.Optional;

@Repository
public interface UserJpaRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.username = :username")
    Optional<User> findByUsername(@Param(value = "username") String username);

    @Query("select u from User u where u.email = :email")
    Optional<User> findByEmail(@Param(value = "email") String email);

    @Query("select u from User u where u.name = :name and u.email = :email")
    Optional<User> findByNameAndEmail(@Param(value = "name") String name,
                                      @Param(value = "email") String email);

    @Query("select u from User u where u.username = :username and u.name = :name and u.email = :email")
    Optional<User> findByUsernameAndNameAndEmail(@Param(value = "username") String username,
                                                 @Param(value = "name") String name,
                                                 @Param(value = "email") String email);
}
