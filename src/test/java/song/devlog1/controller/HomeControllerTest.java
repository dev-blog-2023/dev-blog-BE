package song.devlog1.controller;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import song.devlog1.dto.ResetPasswordDto;
import song.devlog1.entity.ResetPasswordToken;
import song.devlog1.entity.User;
import song.devlog1.repository.ResetPasswordTokenJpaRepository;
import song.devlog1.repository.UserJpaRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ResetPasswordTokenJpaRepository resetPasswordTokenRepository;

    @Autowired
    UserJpaRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @Test
    void login1() throws Exception {
        mockMvc.perform(post("/login")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("username", "a")
                        .param("password", "a"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "a")
    void admin1() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isOk());
    }

    @Test
    @WithUserDetails(value = "b")
    void admin2() throws Exception {
        mockMvc.perform(get("/admin"))
                .andExpect(status().isForbidden());
    }

    @Test
    void findUsername1() throws Exception {
        String jsonRequest = "{\"name\":\"a\", \"email\":\"dkclasltmf22@naver.com\"}";

        mockMvc.perform(post("/findUsername").contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(jsonPath("$.username").value("a"));

    }

    @Test
    void resetPassword1() throws Exception {
        ResetPasswordToken resetPasswordToken = new ResetPasswordToken("a");
        resetPasswordToken.setToken("abc");
        resetPasswordTokenRepository.save(resetPasswordToken);

        String jsonRequest = "{\"newPassword\":\"b\"}";

        mockMvc.perform(post("/resetPassword/{token}", "abc")
                        .contentType(MediaType.APPLICATION_JSON).content(jsonRequest))
                .andExpect(status().isOk());

        User user = userRepository.findById(1L).get();
        Assertions.assertThat(passwordEncoder.matches("b", user.getPassword())).isTrue();
    }
}