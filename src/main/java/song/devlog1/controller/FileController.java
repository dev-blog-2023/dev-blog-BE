package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import song.devlog1.dto.FileUrlDto;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.service.FileEntityService;
import song.devlog1.service.FileService;

import java.io.IOException;
import java.net.MalformedURLException;

@Slf4j
@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;
    private final FileEntityService fileEntityService;

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @PostMapping("/uploadFile")
    public UploadFileDto postUpload(@RequestParam MultipartFile uploadFile) throws IOException {
        UploadFileDto uploadFileDto = fileService.upload(uploadFile);
        fileEntityService.saveFileEntity(uploadFileDto);

        return uploadFileDto;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "/downloadFile/{fileName}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Resource getFile(@PathVariable(value = "fileName") String fileName) throws MalformedURLException {
        fileService.isExists(fileName);
        return new UrlResource("file:" + fileService.getFullPath(fileName));
    }

    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    @GetMapping("/downloadFile/{fileName}")
    public FileUrlDto getFileUrl(@PathVariable(value = "fileName") String fileName) {
        fileService.isExists(fileName);
        String fullPath = fileService.getFullPath(fileName);

        FileUrlDto fileUrlDto = new FileUrlDto(fullPath);

        return fileUrlDto;
    }
}
