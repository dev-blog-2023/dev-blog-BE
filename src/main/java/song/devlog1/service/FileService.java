package song.devlog1.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import song.devlog1.dto.UploadFileDto;
import song.devlog1.exception.notfound.FileNotFoundException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileService {

    @Value("${upload.path}")
    private String uploadPath;

    public UploadFileDto upload(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) {
            return null;
        }

        String originalFilename = multipartFile.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFilename);
        multipartFile.transferTo(new File(getFullPath(saveFileName)));

        return new UploadFileDto(originalFilename, saveFileName);
    }

    public void isExists(String saveFileName) {
        File file = new File(getFullPath(saveFileName));

        if (!file.exists()) {
            throw new FileNotFoundException("파일을 찾을 수 없습니다.");
        }
    }

    public void delete(List<String> saveFileNamesList) {
        for (String saveFileName : saveFileNamesList) {
            delete(saveFileName);
        }
    }

    public void delete(String saveFileName) {
        File file = new File(getFullPath(saveFileName));

        if (file.exists()) {
            file.delete();
        }
    }

    public String getFullPath(String saveFileName) {
        return uploadPath + saveFileName;
    }

    private String createSaveFileName(String originalFilename) {
        String ext = getExt(originalFilename);
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return originalFilename.substring(0, originalFilename.lastIndexOf(".")) + uuid + "." + ext;
    }

    private String getExt(String originalFilename) {
        int p = originalFilename.lastIndexOf(".");
        return originalFilename.substring(p + 1);
    }

}
