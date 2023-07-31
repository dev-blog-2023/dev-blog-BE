package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import song.devlog1.dto.BoardDto;
import song.devlog1.dto.EditBoardDto;
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

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("/save")
    public BoardDto postSaveBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @ModelAttribute SaveBoardDto saveBoardDto,
                              MultipartFile thumbnail) throws IOException {
        log.info("content = {}", saveBoardDto.getContent());
        UploadFileDto uploadFileDto = fileService.upload(thumbnail);

        Long boardId = boardService.saveBoard(saveBoardDto, userDetails.getId(), uploadFileDto.getFileName());

        Document jsoupDoc = Jsoup.parse(saveBoardDto.getContent());
        Elements imgs = jsoupDoc.select("img");

        List<String> imgList = new ArrayList();
        for (Element img : imgs) {
            String imgUrl = img.attr("src");
            String fileName = imgUrl.substring(imgUrl.lastIndexOf("/") + 1);
            imgList.add(fileName);
        }

        fileEntityService.attachFileToBoard(boardId, imgList);

        BoardDto boardDto = boardService.findById(boardId);

        return boardDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable(value = "id") Long boardId) {
        BoardDto boardDto = boardService.findById(boardId);

        return boardDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("/{id}/edit")
    public void postEditBoard(@PathVariable(value = "id") Long boardId,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestBody EditBoardDto editBoardDto) {
        String content = editBoardDto.getContent();

        Document jsoupDoc = Jsoup.parse(content);

        Elements imgs = jsoupDoc.select("img");

        List<String> editImgList = imgs.stream()
                .map(element -> element.attr("src"))
                .map(this::getFileName)
                .toList();

    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
