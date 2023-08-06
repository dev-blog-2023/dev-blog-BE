package song.devlog1.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import song.devlog1.dto.*;
import song.devlog1.entity.User;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.service.UserService;

@Slf4j
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public UserDto getUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        User findUser = userService.findUser(userDetails.getId());

        UserDto userDto = new UserDto(findUser);

        return userDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editEmail")
    public void postEditEmail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody EditEmailDto editEmailDto) {
        Long id = userService.editEmail(userDetails.getId(), editEmailDto.getEmail());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editPassword")
    public void postEditPassword(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                 @RequestBody EditPasswordDto editPasswordDto) {
        Long id = userService.editPassword(userDetails.getId(), editPasswordDto.getOriginalPassword(), editPasswordDto.getNewPassword());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editUsername")
    public void postEditUsername(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                 @RequestBody EditUsernameDto editUsernameDto) {
        Long id = userService.editUsername(userDetails.getId(), editUsernameDto.getUsername());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/editName")
    public void postEditName(@AuthenticationPrincipal UserDetailsImpl userDetails,
                             @RequestBody EditNameDto editNameDto) {
        Long id = userService.editName(userDetails.getId(), editNameDto.getName());
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/delete")
    public void postDeleteUser(@AuthenticationPrincipal UserDetailsImpl userDetails,
                               HttpServletRequest request) {
        userService.deleteUser(userDetails.getId());

        request.getSession(false).invalidate();
    }
}
