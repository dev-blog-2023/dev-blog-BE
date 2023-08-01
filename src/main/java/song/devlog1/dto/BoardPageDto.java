package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.Board;

import java.time.LocalDateTime;

@Getter @Setter
public class BoardPageDto {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime createDateTime;

    public BoardPageDto(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.createDateTime = board.getCreateDate();
    }
}
