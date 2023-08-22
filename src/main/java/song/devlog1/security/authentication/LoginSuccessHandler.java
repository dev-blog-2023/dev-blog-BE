package song.devlog1.security.authentication;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import song.devlog1.security.authentication.jwt.JwtUtils;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.security.userdetails.UserDetailsServiceImpl;

import java.io.IOException;

@Slf4j
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        log.info("login success, userName = {}", userDetails.getUsername());
        String jwtToken = JwtUtils.generateJwtToken(authentication);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("token", jwtToken);

        response.setStatus(HttpServletResponse.SC_OK);
//        response.setHeader("Access-Control-Allow-Origin", "http://localhost:3000");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(jsonObject.toString());
    }
}
