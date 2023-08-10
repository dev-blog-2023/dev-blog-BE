package song.devlog1.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.Board;

import java.util.Optional;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Long> {

    @EntityGraph(attributePaths = {"writer"})
    @Query("select b from Board b where b.id = :id")
    Optional<Board> findById(@Param(value = "id") Long id);

    @EntityGraph(attributePaths = {"writer"})
    Page<Board> findAll(Pageable pageable);

}
