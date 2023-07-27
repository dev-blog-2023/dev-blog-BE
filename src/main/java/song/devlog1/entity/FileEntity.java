package song.devlog1.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class FileEntity {
    @Id @GeneratedValue
    private Long id;

    @JoinColumn(name = "board_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Board board;

    private String uploadFileName;
    private String saveFileName;

    public FileEntity(String uploadFileName, String saveFileName) {
        this.uploadFileName = uploadFileName;
        this.saveFileName = saveFileName;
    }

    public void setBoard(Board board) {
        this.board = board;
        board.getFileEntityList().add(this);
    }
}
