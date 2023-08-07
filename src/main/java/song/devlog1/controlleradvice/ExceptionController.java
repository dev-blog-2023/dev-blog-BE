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
import song.devlog1.dto.ExceptionDto;
import song.devlog1.dto.ResponseException;
import song.devlog1.exception.AlreadyException;
import song.devlog1.exception.InvalidException;
import song.devlog1.exception.NotFoundException;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

    @ExceptionHandler(value = {AlreadyException.class, InvalidException.class, NotFoundException.class})
    public ResponseEntity<ResponseException> mvcExceptionHandler(Exception e,
                                                                 HttpServletRequest request) {
        HttpStatus status = getHttpStatus(e);
        List<ExceptionDto> messages = getMessages(e);
        ResponseException responseException = getResponseException(status, request, messages);

        return new ResponseEntity<>(responseException, status);
    }

    @ExceptionHandler(value = {MailException.class})
    public ResponseEntity<ResponseException> mailExceptionHandler(Exception e,
                                                                  HttpServletRequest request) {
        HttpStatus status = getHttpStatus(e);
        List<ExceptionDto> messages = getMessages(e);
        ResponseException responseException = getResponseException(status, request, messages);

        return new ResponseEntity<>(responseException, status);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ResponseException> validExceptionHandler(MethodArgumentNotValidException e,
                                                                   HttpServletRequest request) {
        HttpStatus status = getHttpStatus(e);
        List<ExceptionDto> messages = getMessages(e);
        ResponseException responseException = getResponseException(status, request, messages);

        return new ResponseEntity<>(responseException, status);
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ResponseException> exceptionHandler(Exception e,
                                                              HttpServletRequest request) {
        HttpStatus status = getHttpStatus(e);
        List<ExceptionDto> messages = getMessages(e);
        ResponseException responseException = getResponseException(status, request, messages);

        return new ResponseEntity<>(responseException, status);
    }

    private HttpStatus getHttpStatus(Exception e) {
        if (e instanceof NotFoundException) {
            return NOT_FOUND;
        } else if (e instanceof AlreadyException || e instanceof InvalidException) {
            return BAD_REQUEST;
        }
        return INTERNAL_SERVER_ERROR;
    }

    private List<ExceptionDto> getMessages(Exception e) {
        List<ExceptionDto> messages = new ArrayList<>();

        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException argumentNotValidException = (MethodArgumentNotValidException) e;
            argumentNotValidException.getBindingResult().getAllErrors().forEach(error->{
                String field = ((FieldError) error).getField();
                String message = error.getDefaultMessage();
                ExceptionDto exceptionDto = new ExceptionDto(field, message);
                messages.add(exceptionDto);
            });
            return messages;
        } else if (e instanceof MailException) {
            ExceptionDto exceptionDto = new ExceptionDto("메일 전송에 실패했습니다.");
            messages.add(exceptionDto);
            return messages;
        }

        ExceptionDto exceptionDto = new ExceptionDto(e.getMessage());
        messages.add(exceptionDto);

        return messages;
    }

    private ResponseException getResponseException(HttpStatus status, HttpServletRequest request,  List<ExceptionDto> messages) {
        return new ResponseException(
                status, messages, request.getRequestURI());
    }
}
