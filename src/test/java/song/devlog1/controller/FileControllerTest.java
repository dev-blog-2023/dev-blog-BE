package song.devlog1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.service.FileService;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.*;
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
    ObjectMapper objectMapper;
    @Autowired
    FileService fileService;

    @Test
    void uploadFile1() throws Exception {
        String content = "mock";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "uploadFile",
                "mock.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );

        MvcResult mvcResult = mockMvc.perform(multipart("/file/uploadFile").file(mockMultipartFile))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        String uploadFileName = jsonNode.get("uploadFileName").asText();

        log.info(response);
        assertThat(uploadFileName).isEqualTo(mockMultipartFile.getOriginalFilename());
    }

    @Test
    void downloadFile1() throws Exception {
        String content = "mock";
        MockMultipartFile mockMultipartFile = new MockMultipartFile(
                "uploadFile",
                "mock.txt",
                "text/plain",
                content.getBytes(StandardCharsets.UTF_8)
        );
        UploadFileDto uploadFileDto = fileService.upload(mockMultipartFile);

        MvcResult mvcResult = mockMvc.perform(get("/file/downloadFile/{fileName}", uploadFileDto.getFileName())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        byte[] mockMultipartBinary = mockMultipartFile.getBytes();
        byte[] responseBinary = mvcResult.getResponse().getContentAsByteArray();

        assertThat(responseBinary).isEqualTo(mockMultipartBinary);
    }
}