package song.devlog1.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    @ResponseBody
    @GetMapping(value = "/downloadFile/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable(value = "fileName") String fileName) throws MalformedURLException {
        fileService.isExists(fileName);
        UrlResource resource = new UrlResource("file:" + fileService.getFullPath(fileName));
        String contentType = setContentType(fileName);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(resource);
    }

    private String setContentType(String fileName) {
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
            return "image/jpeg";
        } else if (fileName.endsWith(".png")) {
            return "image/png";
        }
        return "application/octet-stream";
    }
}
