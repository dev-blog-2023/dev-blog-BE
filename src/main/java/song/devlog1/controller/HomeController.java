package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import song.devlog1.dto.FindPasswordDto;
import song.devlog1.dto.FindUsernameDto;
import song.devlog1.dto.ResetPasswordDto;
import song.devlog1.dto.SignupDto;
import song.devlog1.service.EmailService;
import song.devlog1.service.EmailVerificationService;
import song.devlog1.service.ResetPasswordTokenService;
import song.devlog1.service.UserService;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final EmailVerificationService emailVerificationService;
    private final EmailService emailService;
    private final UserService userService;
    private final ResetPasswordTokenService resetPasswordTokenService;

    @GetMapping("/")
    public void getHome() {

    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/signup")
    public void postSignup(@RequestBody SignupDto signupDto) {
        Long id = userService.saveUser(signupDto);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyUsername")
    public void postVerifyUsername(@RequestBody String username) {
        userService.validUsername(username);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyEmail")
    public void postVerifyEmail(@RequestBody String email) {
        String token = emailVerificationService.createEmailVerificationToken(email);
        emailService.sendMail(email, "email verification", "token: " + token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/verifyEmail/{token}")
    public void postVerifyEmailToken(@PathVariable(value = "token") String token,
                                     @RequestBody String email) {
        Long id = emailVerificationService.verifyEmailVerificationToken(email, token);
        emailVerificationService.deleteEmailVerificationToken(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("/findUsername")
    public String postFindUsername(@RequestBody FindUsernameDto findUsernameDto) {
        String username = userService.findUsername(findUsernameDto.getName(), findUsernameDto.getEmail());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);

        return jsonObject.toString();
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/findPassword")
    public void postFindPassword(@RequestBody FindPasswordDto findPasswordDto) {
        String username = userService.findPassword(findPasswordDto.getUsername(),
                findPasswordDto.getName(), findPasswordDto.getEmail());

        String token = resetPasswordTokenService.saveResetPasswordToken(username);
        emailService.sendMail(findPasswordDto.getEmail(), "reset password",
                "http://localhost:8080/resetPassword/" + token);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/resetPassword/{token}")
    public void getResetPassword(@PathVariable(value = "token") String token) {
        String username = resetPasswordTokenService.verifyResetPasswordToken(token);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/resetPassword/{token}")
    public void postResetPassword(@PathVariable(value = "token") String token,
                                  @RequestBody ResetPasswordDto resetPasswordDto) {
        String username = resetPasswordTokenService.verifyResetPasswordToken(token);

        Long id = userService.resetPassword(username, resetPasswordDto.getNewPassword());
    }

}
