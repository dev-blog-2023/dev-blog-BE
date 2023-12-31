package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.Comment;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CommentDto {
    private Long id;
    private Long boardId;
    private Long parentId;
    private String writer;
    private Long writerId;
    private boolean isCurrentUserAuthor = false;
    private String content;
    private LocalDateTime createDateTime;
    private List<CommentDto> replyList = new ArrayList<>();

    public CommentDto(Comment findComment) {
        this.id = findComment.getId();
        this.boardId = findComment.getBoard().getId();
        this.parentId = findComment.getParent() != null ? findComment.getParent().getId() : null;
        this.writer = findComment.getWriter().getUsername();
        this.writerId = findComment.getWriter().getId();
        this.content = findComment.getContent();
        this.createDateTime = findComment.getCreateDate();
        this.replyList = findComment.getChildList().stream().map(CommentDto::new).toList();
    }

    public CommentDto(Comment findComment, Long userId) {
        this.id = findComment.getId();
        this.boardId = findComment.getBoard().getId();
        this.parentId = findComment.getParent() != null ? findComment.getParent().getId() : null;
        this.writer = findComment.getWriter().getUsername();
        this.writerId = findComment.getWriter().getId();
        this.isCurrentUserAuthor = userId.equals(findComment.getWriter().getId());
        this.content = findComment.getContent();
        this.createDateTime = findComment.getCreateDate();
        this.replyList = findComment.getChildList().stream().map(comment ->
                new CommentDto(comment, userId)).toList();
    }
}
