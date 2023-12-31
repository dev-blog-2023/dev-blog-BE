package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;
import song.devlog1.entity.Board;

@Getter @Setter
public class SaveBoardDto {
    private String title;
    private String content;

    public Board toEntity() {
        Board board = new Board();
        board.setTitle(this.title);
        board.setContent(this.content);

        return board;
    }
}
