package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import song.devlog1.dto.SaveBoardDto;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.service.BoardService;
import song.devlog1.service.FileEntityService;
import song.devlog1.service.FileService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileEntityService fileEntityService;
    private final FileService fileService;

    @GetMapping("/save")
    public void postSaveBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @ModelAttribute SaveBoardDto saveBoardDto,
                              MultipartFile thumbnail) throws IOException {
        UploadFileDto uploadFileDto = fileService.upload(thumbnail);

        Long boardId = boardService.saveBoard(saveBoardDto, userDetails.getId(), uploadFileDto.getFileName());

        Document parse = Jsoup.parse(saveBoardDto.getContent());
        Elements imgs = parse.select("img");

        List<String> imgList = new ArrayList();
        for (Element img : imgs) {
            String imgUrl = img.attr("src");
            imgList.add(imgUrl);
        }

        fileEntityService.attachFileToBoard(boardId, imgList);
    }
}
