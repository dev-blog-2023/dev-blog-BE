package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.entity.Board;
import song.devlog1.entity.FileEntity;
import song.devlog1.exception.notfound.BoardNotFoundException;
import song.devlog1.exception.notfound.FileEntityNotFoundException;
import song.devlog1.repository.BoardJpaRepository;
import song.devlog1.repository.FileEntityJpaRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileEntityService {

    private final FileEntityJpaRepository fileEntityRepository;
    private final BoardJpaRepository boardRepository;

    @Transactional
    public void saveFileEntity(UploadFileDto uploadFileDto) {
        FileEntity fileEntity = new FileEntity(uploadFileDto.getUploadFileName(), uploadFileDto.getFileName());

        fileEntityRepository.save(fileEntity);
    }

    @Transactional
    public List<String> findFileNameByBoardId(Long boardId) {
        List<FileEntity> fileEntityList = fileEntityRepository.findByBoardId(boardId);

        return fileEntityList.stream()
                .map(FileEntity::getSaveFileName).toList();
    }

    @Transactional
    public void deleteFileEntity(String saveFileName) {

    }

    @Transactional
    public void removeFileEntity(List<String> removeImgList) {
        for (String img : removeImgList) {
            Optional<FileEntity> findFileEntity = fileEntityRepository.findBySaveFileName(img);
            if (findFileEntity.isPresent()) {
                fileEntityRepository.delete(findFileEntity.get());
            }
        }
    }

    @Transactional
    public void attachFileToBoard(Long boardId, List<String> imgList) {
        Board findBoard = getBoardById(boardId);

        for (String saveFileName : imgList) {
            FileEntity fileEntity = getFileEntityBySaveFileName(saveFileName);
            fileEntity.setBoard(findBoard);
        }
    }

    private Board getBoardById(Long boardId) {
        Optional<Board> findBoard = boardRepository.findById(boardId);
        if (findBoard.isEmpty()) {
            throw new BoardNotFoundException("게시글을 찾을 수 없습니다.");
        }
        return findBoard.get();
    }

    private FileEntity getFileEntityBySaveFileName(String saveFileName) {
        Optional<FileEntity> findFileEntity = fileEntityRepository.findBySaveFileName(saveFileName);
        if (findFileEntity.isEmpty()) {
            throw new FileEntityNotFoundException("파일을 찾을 수 없습니다.");
        }
        return findFileEntity.get();
    }
}
