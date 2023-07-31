package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.Board;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardDto {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private LocalDateTime createDateTime;

    public BoardDto(Board findBoard) {
        this.id = findBoard.getId();
        this.title = findBoard.getTitle();
        this.content = findBoard.getContent();
        this.writer = findBoard.getWriter().getUsername();
        this.createDateTime = findBoard.getCreateDate();
    }
}
