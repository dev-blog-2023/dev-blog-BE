package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import song.devlog1.dto.UserDto;
import song.devlog1.entity.User;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.service.UserService;

@Slf4j
@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping
    public UserDto getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User findUser = userService.findUser(userDetails.getId());

        UserDto userDto = new UserDto(findUser);

        return userDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editEmail")
    public void postEditEmail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody String email) {
        Long id = userService.editEmail(userDetails.getId(), email);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editPassword")
    public void postEditPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                 @RequestBody String originalPassword,
                                 @RequestBody String newPassword) {
        Long id = userService.editPassword(userDetails.getId(), originalPassword, newPassword);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editUsername")
    public void postEditUsername(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                 @RequestBody String username) {
        Long id = userService.editUsername(userDetails.getId(), username);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editName")
    public void postEditName(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @RequestBody String name) {
        Long id = userService.editName(userDetails.getId(), name);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void postDeleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.deleteUser(userDetails.getId());
    }
}
