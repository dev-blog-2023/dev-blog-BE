package song.devlog1.security.authentication.jwt;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public abstract class JwtKey {
    public static final Key KEY = Keys.secretKeyFor(SignatureAlgorithm.HS512);
}
