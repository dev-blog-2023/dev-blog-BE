package song.devlog1.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.entity.Comment;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class CommentJpaRepositoryTest {

    @Autowired
    CommentJpaRepository commentRepository;

    @Test
    void find1() {
        Comment findComment = commentRepository.findById(1L).get();

        assertThat(findComment.getBoard().getId()).isEqualTo(1L);
    }

}