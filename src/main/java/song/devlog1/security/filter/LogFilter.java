package song.devlog1.security.filter;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

@Slf4j
public class LogFilter extends GenericFilter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;

        String ipAddress = httpRequest.getHeader("X-Forwarded-For");
        if (ipAddress == null) {
            ipAddress = httpRequest.getRemoteAddr();
        }
        if (ipAddress.indexOf(':') >= 0) {
            ipAddress = ipAddress.split(":")[3];
        }

        String requestURI = httpRequest.getRequestURI();

        String name = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            name = authentication.getName();
        }

        String method = httpRequest.getMethod();
        String origin = httpRequest.getHeader("Origin");

        log.info("[Log Filter] ipAddress = {}, origin = {}, uri = {}, username = {}, method = {}", ipAddress, origin, requestURI, name, method);

        chain.doFilter(request, response);
    }
}
