package song.devlog1.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import song.devlog1.dto.EditEmailDto;
import song.devlog1.dto.EditNameDto;
import song.devlog1.dto.EditPasswordDto;
import song.devlog1.dto.EditUsernameDto;
import song.devlog1.entity.User;
import song.devlog1.repository.UserJpaRepository;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserJpaRepository userJpaRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .alwaysDo(print())
                .build();
    }

    @Test
    @WithUserDetails(value = "a")
    void findUser1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get(("/user")))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        Long findUserId = objectMapper.readTree(response).get("id").asLong();

        User findUser = userJpaRepository.findById(findUserId).get();

        assertThat(findUser.getUsername()).isEqualTo("a");
    }

    @Test
    @WithUserDetails(value = "a")
    void editEmail1() throws Exception {
        EditEmailDto editEmailDto = new EditEmailDto();
        editEmailDto.setEmail("TestNewEmail@email.com");
        String requestJson = objectMapper.writeValueAsString(editEmailDto);

        mockMvc.perform(post("/user/editEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        User findUser = userJpaRepository.findByUsername("a").get();

        assertThat(findUser.getEmail()).isEqualTo(editEmailDto.getEmail());
    }

    @Test
    @WithUserDetails(value = "a")
    void editPassword1() throws Exception {
        EditPasswordDto editPasswordDto = new EditPasswordDto();
        editPasswordDto.setOriginalPassword("a");
        editPasswordDto.setNewPassword("testNewPassword");
        String requestJson = objectMapper.writeValueAsString(editPasswordDto);

        mockMvc.perform(post("/user/editPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        User findUser = userJpaRepository.findByUsername("a").get();

        assertThat(passwordEncoder.matches(editPasswordDto.getNewPassword(), findUser.getPassword()))
                .isTrue();
    }

    @Test
    @WithUserDetails(value = "a")
    void editUsername1() throws Exception {
        EditUsernameDto editUsernameDto = new EditUsernameDto();
        editUsernameDto.setUsername("testNewUsername");
        String requestJson = objectMapper.writeValueAsString(editUsernameDto);

        mockMvc.perform(post("/user/editUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> userJpaRepository.findByUsername("a").get())
                .isInstanceOf(NoSuchElementException.class);
        assertThat(userJpaRepository.findByUsername(editUsernameDto.getUsername()).get()).isNotNull();
    }

    @Test
    @WithUserDetails(value = "a")
    void editName1() throws Exception {
        EditNameDto editNameDto = new EditNameDto();
        editNameDto.setName("testNewName");
        String requestJson = objectMapper.writeValueAsString(editNameDto);

        mockMvc.perform(post("/user/editName")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        User findUser = userJpaRepository.findByUsername("a").get();

        assertThat(findUser.getName()).isEqualTo(editNameDto.getName());
    }

    @Test
    @WithUserDetails(value = "a")
    void delete1() throws Exception {
        mockMvc.perform(post("/user/delete"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/user"))
                .andExpect(status().is4xxClientError());
    }
}