package song.devlog1.security.authentication.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.ActiveProfiles;
import song.devlog1.security.userdetails.UserDetailsServiceImpl;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
@SpringBootTest
@ActiveProfiles("test")
class JwtUtilsTest {

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Test
    void test1() {
        String username = "userA";
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());

        String jwt = JwtUtils.generateJwtToken(authenticationToken);
        Jws<Claims> parseToken = Jwts.parserBuilder()
                .setSigningKey(JwtKey.KEY)
                .build()
                .parseClaimsJws(jwt);

        String subject = parseToken.getBody().getSubject();
        Date issuedAt = parseToken.getBody().getIssuedAt();
        log.info(subject);
        log.info(issuedAt.toString());

        String algorithm = parseToken.getHeader().getAlgorithm();
        log.info(algorithm);

        String signature = parseToken.getSignature();
        log.info(signature);

    }

}