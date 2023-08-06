package song.devlog1.controlleradvice;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import song.devlog1.dto.ResponseException;
import song.devlog1.exception.AlreadyException;
import song.devlog1.exception.InvalidException;
import song.devlog1.exception.NotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    @ExceptionHandler(value = {AlreadyException.class, InvalidException.class, NotFoundException.class})
    public ResponseEntity<ResponseException> mvcExceptionHandler(Exception e,
                                                                 HttpServletRequest request) {
        HttpStatus status = getHttpStatus(e);

        ResponseException responseException = new ResponseException(
                status, e.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(responseException, status);
    }

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<ResponseException> mailExceptionHandler(Exception e,
                                                                  HttpServletRequest request) {
        ResponseException responseException = new ResponseException(
                INTERNAL_SERVER_ERROR, "메일 전송에 실패했습니다.", request.getRequestURI());

        return new ResponseEntity<>(responseException, INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseException> validExceptionHandler(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        HttpStatus status = getHttpStatus(e);

        Map<String, String> messages = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(
                error->{
                    String field = ((FieldError) error).getField();
                    String message = error.getDefaultMessage();
                    messages.put(field, message);
                });

        ResponseException responseException = new ResponseException(
                status, messages, request.getRequestURI());

        return new ResponseEntity<>(responseException, status);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseException> exceptionHandler(Exception e,
                                                              HttpServletRequest request) {
        ResponseException responseException = new ResponseException(
                INTERNAL_SERVER_ERROR, e.getMessage(), request.getRequestURI());

        return new ResponseEntity<>(responseException, INTERNAL_SERVER_ERROR);
    }

    private HttpStatus getHttpStatus(Exception e) {
        if (e instanceof NotFoundException) {
            return NOT_FOUND;
        }
        return BAD_REQUEST;
    }
}
