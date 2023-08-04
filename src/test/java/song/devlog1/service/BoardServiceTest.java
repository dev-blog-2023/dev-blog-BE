package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.*;
import song.devlog1.entity.Board;
import song.devlog1.exception.invalid.InvalidAuthorizedException;
import song.devlog1.exception.notfound.BoardNotFoundException;
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
    void findById1() {
        BoardDto findBoardDto = boardService.findById(1L);

        assertThat(findBoardDto.getId()).isEqualTo(1L);

        assertThat(findBoardDto.getCommentList().size()).isEqualTo(5L);

        log.info("title = {}", findBoardDto.getTitle());
        log.info("content = {}", findBoardDto.getContent());
        log.info("============================");
        for (CommentDto commentDto : findBoardDto.getCommentList()) {
            if (commentDto.getParentId() == null) {
                log.info("comment = {}", commentDto.getContent());

                for (CommentDto reply : commentDto.getReplyList()) {
                    log.info("    reply = {}", reply.getContent());
                }
            }
        }
    }

    @Test
    void findAll1() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<BoardPageDto> boardPage = boardService.findAll(pageRequest);

        assertThat(boardPage.getTotalPages()).isEqualTo(2);
    }

    @Test
    void edit1() {
        EditBoardDto editBoardDto = new EditBoardDto();
        editBoardDto.setTitle("Test Edit Title");
        editBoardDto.setContent("Test Edit Content");
        Long id = boardService.editBoard(1L, 1L, editBoardDto);

        Board findBoard = boardRepository.findById(id).get();

        assertThat(findBoard.getTitle()).isEqualTo(editBoardDto.getTitle());

        assertThatThrownBy(() -> boardService.editBoard(2L, 1L, editBoardDto))
                .isInstanceOf(InvalidAuthorizedException.class);
    }

    @Test
    void delete1() {
        boardService.deleteBoard(1L, 1L);

        assertThatThrownBy(() -> boardService.findById(1L)).isInstanceOf(BoardNotFoundException.class);

        assertThatThrownBy(() -> boardService.deleteBoard(2L, 2L))
                .isInstanceOf(InvalidAuthorizedException.class);
    }

}