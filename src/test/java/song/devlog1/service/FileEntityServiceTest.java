package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.entity.FileEntity;
import song.devlog1.repository.FileEntityJpaRepository;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class FileEntityServiceTest {

    @Autowired
    FileEntityService fileEntityService;
    @Autowired
    FileEntityJpaRepository fileEntityRepository;
    @Autowired
    FileService fileService;

    @Test
    void save1() throws IOException {
        String content = "Test Content";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "Test Multipart File.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );
        UploadFileDto uploadFileDto = fileService.upload(mockMultipartFile);

        Long id = fileEntityService.saveFileEntity(uploadFileDto);

        FileEntity findFileEntity = fileEntityRepository.findById(id).get();

        assertThat(findFileEntity.getUploadFileName()).isEqualTo(mockMultipartFile.getOriginalFilename());
    }

    @Test
    void find1() {
        List<String> fileNameByBoardId = fileEntityService.findFileNameByBoardId(1L);

        assertThat(fileNameByBoardId.size()).isEqualTo(0L);
    }

    @Test
    void attach1() throws IOException {
        Long boardId = 1L;
        MockMultipartFile testMockMultipartJpg = new MockMultipartFile(
                "img",
                "Test Jpg.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "Test Image Content".getBytes()
        );
        UploadFileDto uploadFileDto = fileService.upload(testMockMultipartJpg);

        Long fileEntityId = fileEntityService.saveFileEntity(uploadFileDto);

        List<String> fileList = new ArrayList<>();
        fileList.add(uploadFileDto.getFileName());
        fileEntityService.attachFileToBoard(boardId, fileList);

        FileEntity findFileEntity = fileEntityRepository.findById(fileEntityId).get();

        assertThat(findFileEntity.getBoard().getId()).isEqualTo(boardId);
    }
}