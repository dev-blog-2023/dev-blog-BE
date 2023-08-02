package song.devlog1.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.Board;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class BoardJpaRepositoryTest {

    @Autowired
    BoardJpaRepository boardRepository;

    @Test
    void find1() {
        Board findBoard = boardRepository.findById(1L).get();

        assertThat(findBoard.getTitle()).isEqualTo("Title 0");
    }

}