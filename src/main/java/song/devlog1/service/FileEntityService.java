package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.entity.FileEntity;
import song.devlog1.repository.FileEntityJpaRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileEntityService {

    private final FileEntityJpaRepository fileEntityRepository;

    @Transactional
    public void saveFileEntity(UploadFileDto uploadFileDto) {
        FileEntity fileEntity = new FileEntity(uploadFileDto.getUploadFileName(), uploadFileDto.getFileName());

        fileEntityRepository.save(fileEntity);
    }

    @Transactional
    public void deleteFileEntity(String saveFileName) {

    }
}
