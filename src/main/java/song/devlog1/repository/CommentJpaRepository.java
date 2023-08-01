package song.devlog1.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.Comment;

import java.util.Optional;

@Repository
public interface CommentJpaRepository extends JpaRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"writer", "board", "parent"})
    @Query("select c from Comment c where c.id = :id")
    Optional<Comment> findById(@Param(value = "id") Long id);

}
