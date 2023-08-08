package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.Board;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Long writerId;
    private boolean isCurrentUserAuthor = false;
    private LocalDateTime createDateTime;
    private List<CommentDto> commentList = new ArrayList<>();

    public BoardDto(Board findBoard) {
        this.id = findBoard.getId();
        this.title = findBoard.getTitle();
        this.content = findBoard.getContent();
        this.writer = findBoard.getWriter().getUsername();
        this.writerId = findBoard.getWriter().getId();
        this.createDateTime = findBoard.getCreateDate();
        this.commentList = findBoard.getCommentList().stream().filter(comment -> comment.getParent() == null)
                .map(CommentDto::new).toList();
    }

    public BoardDto(Board findBoard, Long userId) {
        this.id = findBoard.getId();
        this.title = findBoard.getTitle();
        this.content = findBoard.getContent();
        this.writer = findBoard.getWriter().getUsername();
        this.writerId = findBoard.getWriter().getId();
        this.isCurrentUserAuthor = userId.equals(findBoard.getWriter().getId());
        this.createDateTime = findBoard.getCreateDate();
        this.commentList = findBoard.getCommentList().stream().filter(comment -> comment.getParent() == null)
                .map(comment -> new CommentDto(comment, userId)).toList();
    }
}
