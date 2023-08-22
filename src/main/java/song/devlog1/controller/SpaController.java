package song.devlog1.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
public class SpaController {

    @RequestMapping({ "/**/{[path:[^\\.]*}" })
    public String redirect() {
        // Forward to home page so that route is preserved.
        return "forward:/";
    }
}
