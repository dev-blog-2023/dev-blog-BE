package song.devlog1.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import song.devlog1.dto.EditCommentDto;
import song.devlog1.dto.SaveCommentDto;
import song.devlog1.entity.Comment;
import song.devlog1.repository.CommentJpaRepository;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class CommentControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    CommentJpaRepository commentRepository;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithUserDetails(value = "userA")
    void postSave1() throws Exception {
        Long boardId = 1L;
        SaveCommentDto saveCommentDto = new SaveCommentDto();
        saveCommentDto.setContent("Test Comment");
        saveCommentDto.setBoardId(boardId);
        saveCommentDto.setParentId(null);

        String jsonRequest = objectMapper.writeValueAsString(saveCommentDto);

        MvcResult mvcResult = mockMvc.perform(post("/comment/save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.content").value(saveCommentDto.getContent()))
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        Long commentId = jsonNode.get("id").asLong();

        Comment findComment = commentRepository.findById(commentId).get();

        assertThat(findComment.getContent()).isEqualTo(saveCommentDto.getContent());
    }

    @Test
    @WithUserDetails(value = "userA")
    void postEdit1() throws Exception {
        Long commentId = 1L;
        Long boardId = 1L;

        EditCommentDto editCommentDto = new EditCommentDto();
        editCommentDto.setBoardId(boardId);
        editCommentDto.setContent("Test Edit Comment");
        String jsonRequest = objectMapper.writeValueAsString(editCommentDto);

        MvcResult mvcResult = mockMvc.perform(post("/comment/{id}/edit", commentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        Long editCommentId = jsonNode.get("id").asLong();

        Comment findComment = commentRepository.findById(editCommentId).get();

        assertThat(findComment.getContent()).isEqualTo(editCommentDto.getContent());
    }

    @Test
    @WithUserDetails(value = "userA")
    void postDelete1() throws Exception {
        Long commentId = 1L;

        assertThat(commentRepository.findById(commentId).get()).isNotNull();

        mockMvc.perform(post("/comment/{id}/delete", commentId))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> commentRepository.findById(commentId).get())
                .isInstanceOf(NoSuchElementException.class);
    }
}