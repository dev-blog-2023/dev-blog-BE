package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.FileEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileEntityJpaRepository extends JpaRepository<FileEntity, Long> {

    @Query("select f from FileEntity f where f.saveFileName = :saveFileName")
    Optional<FileEntity> findBySaveFileName(@Param(value = "saveFileName") String saveFileName);

    @Query("select f from FileEntity f from f.board.id = :boardId")
    List<FileEntity> findByBoardId(@Param(value = "boardId") Long boardId);

}
