package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.dto.CommentDto;
import song.devlog1.dto.EditCommentDto;
import song.devlog1.dto.SaveCommentDto;
import song.devlog1.entity.Board;
import song.devlog1.entity.Comment;
import song.devlog1.entity.User;
import song.devlog1.exception.invalid.InvalidAuthorizedException;
import song.devlog1.exception.notfound.BoardNotFoundException;
import song.devlog1.exception.notfound.CommentNotFoundException;
import song.devlog1.exception.notfound.UserNotFoundException;
import song.devlog1.repository.BoardJpaRepository;
import song.devlog1.repository.CommentJpaRepository;
import song.devlog1.repository.UserJpaRepository;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentJpaRepository commentRepository;
    private final UserJpaRepository userRepository;
    private final BoardJpaRepository boardRepository;

    @Transactional
    public Long saveComment(Long userId, SaveCommentDto saveCommentDto) {
        User findUser = getUserById(userId);
        Board findBoard = getBoardById(saveCommentDto.getBoardId());

        Comment comment = new Comment(saveCommentDto.getContent());
        comment.setWriter(findUser);
        comment.setBoard(findBoard);

        if (saveCommentDto.getParentId() != null) {
            Comment findComment = getCommentById(saveCommentDto.getParentId());
            comment.setParent(findComment);
        }

        Comment saveComment = commentRepository.save(comment);
        return saveComment.getId();
    }

    @Transactional
    public Long editComment(Long userId, Long commentId, EditCommentDto editCommentDto) {
        Comment findComment = getCommentById(commentId);

        if (!findComment.getWriter().getId().equals(userId)) {
            throw new InvalidAuthorizedException("권한이 없습니다.");
        }

        findComment.setContent(editCommentDto.getContent());

        Comment saveComment = commentRepository.save(findComment);
        return saveComment.getId();
    }

    @Transactional
    public CommentDto findCommentById(Long commentId) {
        Comment findComment = getCommentById(commentId);

        CommentDto commentDto = new CommentDto(findComment);

        return commentDto;
    }

    @Transactional
    public void deleteComment(Long userId, Long commentId) {
        Comment findComment = getCommentById(commentId);

        if (!findComment.getWriter().getId().equals(userId)) {
            throw new InvalidAuthorizedException("권한이 없습니다.");
        }

        commentRepository.delete(findComment);
    }

    private User getUserById(Long userId) {
        Optional<User> findUser = userRepository.findById(userId);
        if (findUser.isEmpty()) {
            throw new UserNotFoundException("사용자를 찾을 수 없습니다.");
        }
        return findUser.get();
    }

    private Board getBoardById(Long boardId) {
        Optional<Board> findBoard = boardRepository.findById(boardId);
        if (findBoard.isEmpty()) {
            throw new BoardNotFoundException("게시글을 찾을 수 없습니다.");
        }
        return findBoard.get();
    }

    private Comment getCommentById(Long commentId) {
        Optional<Comment> findComment = commentRepository.findById(commentId);
        if (findComment.isEmpty()) {
            throw new CommentNotFoundException("댓글을 찾을 수 없습니다.");
        }
        return findComment.get();
    }
}
