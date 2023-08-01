package song.devlog1.controlleradvice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import song.devlog1.exception.AlreadyException;
import song.devlog1.exception.InvalidException;
import song.devlog1.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    @ExceptionHandler(value = {AlreadyException.class, InvalidException.class, NotFoundException.class})
    public ResponseEntity<String> mvcExceptionHandler(Exception e) {
        return ResponseEntity.badRequest()
                .body(e.getMessage());
    }

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<String> mailExceptionHandler(Exception e) {
        return ResponseEntity.badRequest()
                .body("이메일 전송에 실패했습니다.");
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> validExceptionHandler(MethodArgumentNotValidException e) {
        HashMap<String, String> errorMap = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(exception->{
            String field = ((FieldError) exception).getField();
            String message = exception.getDefaultMessage();
            errorMap.put(field, message);
        });

        return ResponseEntity.badRequest()
                .body(errorMap);
    }
}
