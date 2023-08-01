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
    private LocalDateTime createDateTime;
    private List<CommentDto> commentList = new ArrayList<>();

    public BoardDto(Board findBoard) {
        this.id = findBoard.getId();
        this.title = findBoard.getTitle();
        this.content = findBoard.getContent();
        this.writer = findBoard.getWriter().getUsername();
        this.createDateTime = findBoard.getCreateDate();
        this.commentList = findBoard.getCommentList().stream().map(CommentDto::new).toList();
    }
}
