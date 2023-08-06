package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import song.devlog1.dto.BoardDto;
import song.devlog1.dto.EditBoardDto;
import song.devlog1.dto.SaveBoardDto;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.service.BoardService;
import song.devlog1.service.FileEntityService;
import song.devlog1.service.FileService;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/board")
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final FileEntityService fileEntityService;
    private final FileService fileService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/save")
    public BoardDto postSaveBoard(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @RequestBody SaveBoardDto saveBoardDto) {
        Long boardId = boardService.saveBoard(saveBoardDto, userDetails.getId());

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
    @GetMapping("/{id}")
    public BoardDto getBoard(@PathVariable(value = "id") Long boardId) {
        BoardDto boardDto = boardService.findById(boardId);

        return boardDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/edit")
    public BoardDto postEditBoard(@PathVariable(value = "id") Long boardId,
                                  @AuthenticationPrincipal UserDetailsImpl userDetails,
                                  @RequestBody EditBoardDto editBoardDto) {
        Long id = boardService.editBoard(userDetails.getId(), boardId, editBoardDto);
        String content = editBoardDto.getContent();

        Document jsoupDoc = Jsoup.parse(content);

        Elements imgs = jsoupDoc.select("img");

        List<String> editImgList = imgs.stream()
                .map(element -> element.attr("src"))
                .map(this::getFileName)
                .toList();

        List<String> boardFileList = fileEntityService.findFileNameByBoardId(boardId);

        List<String> addImgList = editImgList.stream()
                .filter(editImg -> !boardFileList.contains(editImg))
                .toList();

        List<String> removeImgList = boardFileList.stream()
                .filter(boardImg -> !editImgList.contains(boardImg))
                .toList();

        fileEntityService.attachFileToBoard(boardId, addImgList);

        for (String img : removeImgList) {
            fileEntityService.deleteFileEntity(img);
            fileService.delete(img);
        }

        BoardDto boardDto = boardService.findById(id);

        return boardDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/{id}/delete")
    public void postDeleteBoard(@PathVariable(value = "id") Long boardId,
                                @AuthenticationPrincipal UserDetailsImpl userDetails) {
        boardService.deleteBoard(userDetails.getId(), boardId);
        fileEntityService.deleteByBoardId(boardId);
    }

    private String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }
}
