package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.BoardDto;
import song.devlog1.dto.CommentDto;
import song.devlog1.dto.SaveBoardDto;
import song.devlog1.entity.Board;
import song.devlog1.repository.BoardJpaRepository;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class BoardServiceTest {

    @Autowired
    BoardService boardService;
    @Autowired
    BoardJpaRepository boardRepository;

    @Test
    void save1() {
        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle("Test Title");
        saveBoardDto.setContent("Test Content");
        Long id = boardService.saveBoard(saveBoardDto, 1L);

        Board findBoard = boardRepository.findById(id).get();

        assertThat(findBoard.getTitle()).isEqualTo(saveBoardDto.getTitle());
    }

    @Test
    void find1() {
        BoardDto findBoardDto = boardService.findById(1L);

        assertThat(findBoardDto.getId()).isEqualTo(1L);

        assertThat(findBoardDto.getCommentList().size()).isEqualTo(5L);

        log.info("title = {}", findBoardDto.getTitle());
        log.info("content = {}", findBoardDto.getContent());
        log.info("============================");
        for (CommentDto commentDto : findBoardDto.getCommentList()) {
            if (commentDto.getParentId() == null) {
                log.info("comment = {}", commentDto.getContent());

                for (java.lang.Object o : commentDto.getre) {

                }
            }
        }
    }

}