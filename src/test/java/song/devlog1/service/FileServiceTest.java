package song.devlog1.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.dto.UploadFileDto;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FileServiceTest {

    @Autowired
    FileService fileService;

    @Value(value = "${upload.path}")
    String path;

    @Value(value = "${upload.springPng}")
    String springPng;

    @Value(value = "${upload.securityPng}")
    String securityPng;


    @Test
    void upload1() throws IOException {
        String content = "Test Content";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "Test Mock Multipart.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );
        UploadFileDto uploadFileDto = fileService.upload(mockMultipartFile);

        File file = new File(path + uploadFileDto.getFileName());
        assertThat(file.exists()).isTrue();
    }

    @Test
    void delete1() throws Exception {
        String content = "Test Content";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "Test Mock Multipart.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );
        UploadFileDto uploadFileDto = fileService.upload(mockMultipartFile);

        fileService.delete(uploadFileDto.getFileName());

        File file = new File(path + uploadFileDto.getFileName());
        assertThat(file.exists()).isFalse();
    }

    @Test
    void find1() {
        File file = new File(springPng);
        if (file.exists()) {
            log.info(file.getName());
        }
    }
}