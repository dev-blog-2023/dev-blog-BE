package song.devlog1.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import song.devlog1.dto.SaveBoardDto;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.service.FileEntityService;
import song.devlog1.service.FileService;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    FileService fileService;
    @Autowired
    FileEntityService fileEntityService;

    @WithUserDetails(value = "a")
    @Test
    void saveBoard1() throws Exception {
        String content = "mulipart";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "file",
                "mockfile.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
                );
        UploadFileDto uploadFileDto = fileService.upload(mockMultipartFile);

        fileEntityService.saveFileEntity(uploadFileDto);

        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle("test title");
        saveBoardDto.setContent("<p>test content</p> <img src=" + uploadFileDto.getUrl() + ">");

        MockMultipartFile thumbnail = new MockMultipartFile(
                "thumbnail",
                "test.jpg",
                MediaType.IMAGE_JPEG_VALUE,
                "test image content".getBytes()
        );

        mockMvc.perform(multipart("/board/save")
                        .file(thumbnail).flashAttr("saveBoardDto", saveBoardDto))
                .andExpect(status().isOk());

    }

}