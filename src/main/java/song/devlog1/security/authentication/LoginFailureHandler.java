package song.devlog1.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class LoginFailureHandler implements AuthenticationFailureHandler {
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String message = "알 수 없는 오류가 발생하였습니다.";
        if (exception instanceof BadCredentialsException || exception instanceof UsernameNotFoundException) {
            message = "아이디 또는 비밀번호가 일치하지 않습니다.";
        } else if (exception instanceof AccountExpiredException) {
            message = "만료된 계정입니다.";
        } else if (exception instanceof LockedException) {
            message = "잠긴 계정입니다.";
        } else if (exception instanceof CredentialsExpiredException) {
            message = "인증 정보가 만료된 계정입니다.";
        } else if (exception instanceof DisabledException) {
            message = "비활성화된 계정입니다.";
        }

        JSONObject jsonObject = new JSONObject();
        JSONObject error = jsonObject.put("error", message);

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(error.toString());
    }
}
