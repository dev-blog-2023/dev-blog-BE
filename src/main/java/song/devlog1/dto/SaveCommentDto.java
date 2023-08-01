package song.devlog1.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class SaveCommentDto {
    private Long boardId;
    private Long parentId;
    private String content;
}
