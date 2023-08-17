package song.devlog1.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import song.devlog1.dto.*;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.service.*;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

    private final EmailVerificationService emailVerificationService;
    private final ResetPasswordTokenService resetPasswordTokenService;
    private final EmailService emailService;
    private final UserService userService;
    private final BoardService boardService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/")
    public Page<BoardPageDto> getHome(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<BoardPageDto> boardPageDtoPage = boardService.findAll(pageable);

        return boardPageDtoPage;
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void postSignup(@Valid @RequestBody SignupDto signupDto) {
        Long id = userService.saveUser(signupDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyUsername")
    public void postVerifyUsername(@Valid @RequestBody VerifyUsernameDto verifyUsernameDto) {
        userService.validUsername(verifyUsernameDto.getUsername());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyEmail")
    public void postVerifyEmail(@Valid @RequestBody VerifyEmailDto verifyEmailDto) {
        String token = emailVerificationService.createEmailVerificationToken(verifyEmailDto.getEmail());
        emailService.sendMail(verifyEmailDto.getEmail(), "email verification", "token: " + token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyEmail/{token}")
    public void postVerifyEmailToken(@PathVariable(value = "token") String token,
                                     @Valid @RequestBody VerifyEmailDto verifyEmailDto) {
        Long id = emailVerificationService.verifyEmailVerificationToken(verifyEmailDto.getEmail(), token);
        emailVerificationService.deleteEmailVerificationToken(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/findUsername")
    public String postFindUsername(@Valid @RequestBody FindUsernameDto findUsernameDto) {
        String username = userService.findUsername(findUsernameDto.getName(), findUsernameDto.getEmail());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);

        return jsonObject.toString();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/findPassword")
    public void postFindPassword(@Valid @RequestBody FindPasswordDto findPasswordDto) {
        String username = userService.findPassword(findPasswordDto.getUsername(),
                findPasswordDto.getName(), findPasswordDto.getEmail());

        String token = resetPasswordTokenService.saveResetPasswordToken(username);
        emailService.sendMail(findPasswordDto.getEmail(), "reset password",
                "http://52.79.222.161:8080/resetPassword/" + token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/resetPassword/{token}")
    public void getResetPassword(@PathVariable(value = "token") String token) {
        String username = resetPasswordTokenService.verifyResetPasswordToken(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/resetPassword/{token}")
    public void postResetPassword(@PathVariable(value = "token") String token,
                                  @Valid @RequestBody ResetPasswordDto resetPasswordDto) {
        String username = resetPasswordTokenService.verifyResetPasswordToken(token);

        Long id = userService.resetPassword(username, resetPasswordDto.getNewPassword());
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/ud")
    public UserDetailsImpl getud(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return userDetails;
    }
}
