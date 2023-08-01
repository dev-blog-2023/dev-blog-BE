package song.devlog1.security.authentication.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import song.devlog1.security.userdetails.UserDetailsImpl;

import java.util.Date;

import static song.devlog1.security.authentication.jwt.JwtKey.*;

@Slf4j
public class JwtUtils {

    public static String generateJwtToken(Authentication authentication) {
        UserDetailsImpl principal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(principal.getUsername())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }
}
