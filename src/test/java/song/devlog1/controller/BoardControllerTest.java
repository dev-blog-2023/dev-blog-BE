package song.devlog1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import song.devlog1.dto.EditBoardDto;
import song.devlog1.dto.SaveBoardDto;
import song.devlog1.entity.Board;
import song.devlog1.repository.BoardJpaRepository;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
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
    ObjectMapper objectMapper;
    @Autowired
    BoardJpaRepository boardRepository;

    @Test
    @WithUserDetails(value = "a")
    void save1() throws Exception {
        SaveBoardDto saveBoardDto = new SaveBoardDto();
        saveBoardDto.setTitle("Test Title");
        saveBoardDto.setContent("<p>Test Content</p>");
        String requestJson = objectMapper.writeValueAsString(saveBoardDto);

        MvcResult mvcResult = mockMvc.perform(post("/board/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Long saveBoardId = objectMapper.readTree(response).get("id").asLong();

        Board findSaveBoard = boardRepository.findById(saveBoardId).get();

        assertThat(findSaveBoard.getContent()).isEqualTo(saveBoardDto.getContent());
    }

    @Test
    void find1() throws Exception {
        Long boardId = 1L;
        MvcResult mvcResult = mockMvc.perform(get("/board/{boardId}", boardId))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();

        JsonNode jsonNode = objectMapper.readTree(response);
        Long findBoardId = jsonNode.get("id").asLong();

        assertThat(findBoardId).isEqualTo(boardId);
    }

    @Test
    @WithUserDetails(value = "a")
    void edit1() throws Exception {
        Long boardId = 1L;

        EditBoardDto editBoardDto = new EditBoardDto();
        editBoardDto.setTitle("Test Edit Title");
        editBoardDto.setContent("<p>Test Edit Content</p>");
        String requestJson = objectMapper.writeValueAsString(editBoardDto);

        MvcResult mvcResult = mockMvc.perform(post("/board/{boardId}/edit", boardId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        Long editBoardId = jsonNode.get("id").asLong();

        Board findEditBoard = boardRepository.findById(editBoardId).get();

        assertThat(editBoardDto.getContent()).isEqualTo(findEditBoard.getContent());
    }

    @Test
    @WithUserDetails(value = "a")
    void delete1() throws Exception {
        Long boardId = 1L;
        mockMvc.perform(post("/board/{boardId}/delete", boardId))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> boardRepository.findById(boardId).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @WithUserDetails(value = "b")
    void delete2() throws Exception {
        Long boardId = 1L;
        MvcResult mvcResult = mockMvc.perform(post("/board/{boardId}/delete", boardId))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String responseJson = objectMapper.readTree(response).toPrettyString();

        log.info(responseJson);
    }

}