package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.Board;

@Repository
public interface BoardJpaRepository extends JpaRepository<Board, Long> {

}
