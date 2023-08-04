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
    public Long saveFileEntity(UploadFileDto uploadFileDto) {
        FileEntity fileEntity = new FileEntity(uploadFileDto.getUploadFileName(), uploadFileDto.getFileName());

        FileEntity saveFileEntity = fileEntityRepository.save(fileEntity);

        return saveFileEntity.getId();
    }

    @Transactional
    public List<String> findFileNameByBoardId(Long boardId) {
        List<FileEntity> fileEntityList = getFileEntityByBoardId(boardId);

        return fileEntityList.stream()
                .map(FileEntity::getSaveFileName).toList();
    }

    @Transactional
    public void deleteFileEntity(String saveFileName) {
        Optional<FileEntity> findFileEntity = fileEntityRepository.findBySaveFileName(saveFileName);
        if (findFileEntity.isEmpty()) {
            return;
        }

        fileEntityRepository.delete(findFileEntity.get());
    }

    @Transactional
    public void deleteFileEntity(List<String> removeImgList) {
        for (String img : removeImgList) {
            deleteFileEntity(img);
        }
    }

    @Transactional
    public void deleteByBoardId(Long boardId) {
        List<FileEntity> findFileEntityList = getFileEntityByBoardId(boardId);
        for (FileEntity fileEntity : findFileEntityList) {
            fileEntityRepository.delete(fileEntity);
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

    private List<FileEntity> getFileEntityByBoardId(Long boardId) {
        return fileEntityRepository.findByBoardId(boardId);
    }
}
