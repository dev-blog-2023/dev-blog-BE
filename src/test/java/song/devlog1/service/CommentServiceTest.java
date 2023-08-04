package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.CommentDto;
import song.devlog1.dto.EditCommentDto;
import song.devlog1.dto.SaveCommentDto;
import song.devlog1.entity.Comment;
import song.devlog1.exception.invalid.InvalidAuthorizedException;
import song.devlog1.exception.notfound.CommentNotFoundException;
import song.devlog1.repository.CommentJpaRepository;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class CommentServiceTest {

    @Autowired
    CommentService commentService;
    @Autowired
    CommentJpaRepository commentRepository;

    @Test
    void save1() {
        SaveCommentDto saveCommentDto = new SaveCommentDto();
        saveCommentDto.setContent("Test Comment");
        saveCommentDto.setBoardId(1L);
        saveCommentDto.setParentId(null);
        Long id = commentService.saveComment(1L, saveCommentDto);

        Comment findComment = commentRepository.findById(id).get();

        assertThat(findComment.getContent()).isEqualTo(saveCommentDto.getContent());
    }

    @Test
    void edit1() {
        EditCommentDto editCommentDto = new EditCommentDto();
        editCommentDto.setContent("Test Edit Comment");
        editCommentDto.setBoardId(1L);

        Long id = commentService.editComment(1L, 1L, editCommentDto);

        Comment findComment = commentRepository.findById(id).get();

        assertThat(findComment.getContent()).isEqualTo(editCommentDto.getContent());
    }

    @Test
    void findById1() {
        CommentDto findCommentDto = commentService.findCommentById(1L);

        log.info(findCommentDto.getContent());
        log.info(findCommentDto.getWriter());

        assertThatThrownBy(() -> commentService.findCommentById(-1L))
                .isInstanceOf(CommentNotFoundException.class);
    }

    @Test
    void delete1() {
        commentService.deleteComment(1L, 1L);

        Optional<Comment> findComment = commentRepository.findById(1L);
        assertThatThrownBy(() -> findComment.get()).isInstanceOf(NoSuchElementException.class);

        assertThatThrownBy(() -> commentService.deleteComment(2L, 2L))
                .isInstanceOf(InvalidAuthorizedException.class);
    }


}