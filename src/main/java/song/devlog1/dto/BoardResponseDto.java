package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class BoardResponseDto {
    private Long boardId;

    public BoardResponseDto(Long boardId) {
        this.boardId = boardId;
    }
}
