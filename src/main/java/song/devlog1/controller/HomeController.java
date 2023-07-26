package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {


    @GetMapping("/")
    public void getHome() {

    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/admin")
    public void getAdmin() {
        log.info("ok");
    }
}
