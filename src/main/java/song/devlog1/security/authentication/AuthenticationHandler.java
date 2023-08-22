package song.devlog1.security.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import song.devlog1.dto.ExceptionDto;
import song.devlog1.dto.ResponseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AuthenticationHandler implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = request.getRemoteAddr();
        }
        if (ipAddress.indexOf(':') >= 0) {
            ipAddress = ipAddress.split(":")[3];
        }
        String origin = request.getHeader("Origin");
        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        log.info("[Authentication Fail] ipAddress = {}, origin = {}, uri = {}, method = {}", ipAddress, origin, requestURI, method);

        ExceptionDto exceptionDto = new ExceptionDto(authException.getMessage());
        List<ExceptionDto> messages = new ArrayList<>();
        messages.add(exceptionDto);

        ResponseException responseException = new ResponseException(HttpStatus.UNAUTHORIZED, messages, request.getRequestURI());

        String exceptionJson = objectMapper.writeValueAsString(responseException);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setHeader("Access-Control-Allow-Origin", "http://localhost:8080");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(exceptionJson);
    }
}
