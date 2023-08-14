package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
public class OAuthController {

//    @GetMapping("/oauth/naver")
    public String oAuth() {

        return "redirect:/oauth2/authorization/naver";
    }
}
