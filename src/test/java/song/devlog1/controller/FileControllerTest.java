package song.devlog1.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.entity.FileEntity;
import song.devlog1.repository.FileEntityJpaRepository;
import song.devlog1.service.FileService;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class FileControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    FileService fileService;
    @Autowired
    FileEntityJpaRepository fileEntityRepository;

    @Test
    void saveFile1() throws Exception {
        String content = "mock";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "uploadFile",
                "mock.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        mockMvc.perform(multipart("/file/uploadFile").file(mockMultipartFile))
                .andExpect(status().isOk());

        List<FileEntity> fileEntityList = fileEntityRepository.findAll();
        Assertions.assertThat(fileEntityList.size()).isEqualTo(1L);

    }

    @Test
    void saveFile2() throws Exception {
        String content = "mock";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "uploadFile",
                "mock.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        UploadFileDto uploadFileDto = fileService.upload(mockMultipartFile);

        mockMvc.perform(get("/file/downloadFile/{fileName}", uploadFileDto.getFileName()))
                .andExpect(status().isOk());

    }
}