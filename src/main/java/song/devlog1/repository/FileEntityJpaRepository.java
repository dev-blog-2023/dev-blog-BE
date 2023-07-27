package song.devlog1.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import song.devlog1.entity.FileEntity;

@Repository
public interface FileEntityJpaRepository extends JpaRepository<FileEntity, Long> {

}
