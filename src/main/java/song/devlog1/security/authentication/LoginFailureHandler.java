package song.devlog1.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import song.devlog1.dto.ExceptionDto;
import song.devlog1.dto.ResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class LoginFailureHandler implements AuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.info("username = {}, password = {}", request.getParameter("username"), request.getParameter("password"));

        HttpStatus status = INTERNAL_SERVER_ERROR;
        String message = "예외가 발생했습니다.";
        List<ExceptionDto> messages = new ArrayList<>();

        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            message = "아이디 또는 비밀번호가 일치하지 않습니다.";
            messages.add(new ExceptionDto(message));
            status = UNAUTHORIZED;
        } else if (exception instanceof AccountExpiredException) {
            message = "만료된 계정입니다.";
            messages.add(new ExceptionDto(message));
            status = UNAUTHORIZED;
        } else if (exception instanceof LockedException) {
            message = "잠긴 계정입니다.";
            messages.add(new ExceptionDto(message));
            status = UNAUTHORIZED;
        } else if (exception instanceof CredentialsExpiredException) {
            message = "인증 정보가 만료된 계정입니다.";
            messages.add(new ExceptionDto(message));
            status = UNAUTHORIZED;
        } else if (exception instanceof DisabledException) {
            message = "비활성화된 계정입니다.";
            messages.add(new ExceptionDto(message));
            status = UNAUTHORIZED;
        } else {
            message = "알 수 없는 오류가 발생하였습니다.";
            messages.add(new ExceptionDto(message));
            status = INTERNAL_SERVER_ERROR;
        }

        ResponseException responseException = new ResponseException(status, messages, request.getRequestURI());
        String exceptionJson = objectMapper.writeValueAsString(responseException);

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(exceptionJson);
    }
}
