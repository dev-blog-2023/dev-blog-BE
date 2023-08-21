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
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import song.devlog1.dto.*;
import song.devlog1.entity.ResetPasswordToken;
import song.devlog1.entity.User;
import song.devlog1.repository.EmailVerificationJpaRepository;
import song.devlog1.repository.ResetPasswordTokenJpaRepository;
import song.devlog1.repository.UserJpaRepository;
import song.devlog1.service.EmailVerificationService;

import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class HomeControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    WebApplicationContext webApplicationContext;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    UserJpaRepository userRepository;
    @Autowired
    EmailVerificationService emailVerificationService;
    @Autowired
    EmailVerificationJpaRepository emailVerificationRepository;

    @Autowired
    ResetPasswordTokenJpaRepository resetPasswordTokenRepository;
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
    void home1() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(response);
        Long pageNumber = jsonNode.get("pageable").get("pageNumber").asLong();

        assertThat(pageNumber).isEqualTo(0L);
    }

    @Test
    void signup1() throws Exception {
        SignupDto signupDto = new SignupDto();
        signupDto.setUsername("Test Signup Username");
        signupDto.setName("Test Signup Name");
        signupDto.setEmail("TestSignup@email.com");
        signupDto.setPassword("Test Signup Password");
        String requestJson = objectMapper.writeValueAsString(signupDto);

        mockMvc.perform(post("/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated());

        assertThat(userRepository.findByUsername(signupDto.getUsername()).get()).isNotNull();

        User findUser = userRepository.findByUsername(signupDto.getUsername()).get();
        assertThat(findUser.getUsername()).isEqualTo(signupDto.getUsername());
    }

    @Test
    void verifyUsername1() throws Exception {
        VerifyUsernameDto usableUsernameDto = new VerifyUsernameDto();
        usableUsernameDto.setUsername("Test Verify Username");
        String requestJson = objectMapper.writeValueAsString(usableUsernameDto);

        mockMvc.perform(post("/verifyUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void verifyUsername2() throws Exception {
        VerifyUsernameDto AlreadyUsernameDto = new VerifyUsernameDto();
        AlreadyUsernameDto.setUsername("userA");
        String requestJson = objectMapper.writeValueAsString(AlreadyUsernameDto);

        mockMvc.perform(post("/verifyUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError());
    }

    @Test
    void verifyEmail1() throws Exception {
        VerifyEmailDto verifyEmailDto = new VerifyEmailDto();
        verifyEmailDto.setEmail("dkclasltmf@naver.com");
        String requestJson = objectMapper.writeValueAsString(verifyEmailDto);

        mockMvc.perform(post("/verifyEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());
    }

    @Test
    void verifyEmail2() throws Exception {
        VerifyEmailDto verifyEmailDto = new VerifyEmailDto();
        verifyEmailDto.setEmail("emailException");
        String requestJson = objectMapper.writeValueAsString(verifyEmailDto);

        mockMvc.perform(post("/verifyEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void verifyEmail3() throws Exception {
        VerifyEmailDto verifyEmailDto = new VerifyEmailDto();
        verifyEmailDto.setEmail("dkclasltmf22@naver.com");
        String requestJson = objectMapper.writeValueAsString(verifyEmailDto);

        MvcResult mvcResult = mockMvc.perform(post("/verifyEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().is4xxClientError())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String responseException = objectMapper.readTree(response).toPrettyString();
        log.info(responseException);
    }

    @Test
    void verifyEmailToken1() throws Exception {
        VerifyEmailDto verifyEmailDto = new VerifyEmailDto();
        verifyEmailDto.setEmail("Test Verify Email");
        String requestJson = objectMapper.writeValueAsString(verifyEmailDto);

        String token = emailVerificationService.createEmailVerificationToken(verifyEmailDto.getEmail());

        mockMvc.perform(post("/verifyEmail/{token}", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        assertThatThrownBy(() -> emailVerificationRepository.findByEmail(verifyEmailDto.getEmail()).get())
                .isInstanceOf(NoSuchElementException.class);
    }

    @Test
    void findUsername1() throws Exception {
        FindUsernameDto findUsernameDto = new FindUsernameDto();
        findUsernameDto.setName("홍길동");
        findUsernameDto.setEmail("dkclasltmf22@naver.com");
        String requestJson = objectMapper.writeValueAsString(findUsernameDto);

        MvcResult mvcResult = mockMvc.perform(post("/findUsername")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andReturn();

        String response = mvcResult.getResponse().getContentAsString();
        String username = objectMapper.readTree(response).get("username").asText();

        assertThat(userRepository.findByUsername(username).get().getName())
                .isEqualTo(findUsernameDto.getName());
    }

    @Test
    void findPassword1() throws Exception {
        /**
         * post find password
         */
        FindPasswordDto findPasswordDto = new FindPasswordDto();
        findPasswordDto.setUsername("userA");
        findPasswordDto.setName("홍길동");
        findPasswordDto.setEmail("dkclasltmf22@naver.com");
        String requestJson = objectMapper.writeValueAsString(findPasswordDto);

        mockMvc.perform(post("/findPassword")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk());

        ResetPasswordToken resetPasswordToken = resetPasswordTokenRepository.
                findByUsername(findPasswordDto.getUsername()).get();

        String token = resetPasswordToken.getToken();

        /**
         * post reset password
         */
        ResetPasswordDto resetPasswordDto = new ResetPasswordDto();
        resetPasswordDto.setNewPassword("testNewPassword");
        String resetPasswordJson = objectMapper.writeValueAsString(resetPasswordDto);

        mockMvc.perform(post("/resetPassword/{token}", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(resetPasswordJson))
                .andExpect(status().isOk());

        User findUser = userRepository.findByUsername(findPasswordDto.getUsername()).get();
        assertThat(passwordEncoder.matches(resetPasswordDto.getNewPassword(), findUser.getPassword()))
                .isTrue();
    }

    @Test
    void cors1() throws Exception {
        VerifyEmailDto verifyEmailDto = new VerifyEmailDto();
        verifyEmailDto.setEmail("dkclasltmf@naver.com");
        String requestJson = objectMapper.writeValueAsString(verifyEmailDto);

        mockMvc.perform(post("/verifyEmail")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson)
                        .header("Origin", "http://localhost:3030"))
                .andExpect(status().isOk())
                .andExpect(header().exists("Access-Control-Allow-Origin"));
    }
}