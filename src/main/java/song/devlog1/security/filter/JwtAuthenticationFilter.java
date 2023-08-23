package song.devlog1.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.filter.OncePerRequestFilter;
import song.devlog1.exception.JwtProcessException;
import song.devlog1.security.userdetails.UserDetailsImpl;
import song.devlog1.security.userdetails.UserDetailsServiceImplInterface;

import java.io.IOException;

import static song.devlog1.security.authentication.jwt.JwtKey.*;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final UserDetailsServiceImplInterface userDetailsService;
    private final AuthenticationEntryPoint authenticationHandler;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String token = authorization.split(" ")[1].trim();
            Authentication authentication = getAuthentication(token);
            if (authentication != null) {
                SecurityContextHolder.getContext().setAuthentication(authentication);
                log.info("[Jwt Filter] set context");
            }
        } catch (Exception e) {
            authenticationHandler.commence(request, response, new JwtProcessException(e.getMessage(), e));
            return;
        }

        filterChain.doFilter(request, response);
    }

    private Authentication getAuthentication(String token) {
        Jws<Claims> parsedToken = Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(token);

        Long id = Long.valueOf(parsedToken
                .getBody()
                .getSubject());

        if (id != null) {
            UserDetails userDetails = userDetailsService.loadUserById(id);
            return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        }

        return null;
    }
}
