package co.com.pragma.crediya.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Service
public class JwtService {

    private final SecretKey key;
    private final String issuer;
    private final String audience;
    private final Long ttlMinutes;

    public JwtService(
            @Value("${security.jwt.secret}") String secret,
            @Value("${app.issuer}") String issuer,
            @Value("${app.audience}") String audience,
            @Value("${security.jwt.ttl-minutes}") Long ttlMinutes) {

        // soporta secreto en base64 o texto plano largo
        byte[] bytes = secret.matches("^[A-Za-z0-9+/=]+$") ? Decoders.BASE64.decode(secret) : secret.getBytes(StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.issuer = issuer;
        this.audience = audience;
        this.ttlMinutes = ttlMinutes;
    }

    public String generate(String userId, String email, Set<String> roles) {
        Instant now = Instant.now();
        return Jwts.builder()
                .issuer(issuer)
                .audience().add(audience).and()
                .subject(userId)               // usa el ID estable como "sub"
                .claim("uid", userId)
                .claim("email", email)
                .claim("roles", roles)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt()))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }

    public Instant expiresAt() {
        return Instant.now().plus(Duration.ofMinutes(ttlMinutes));
    }

    public Long getttlMinutes(){
        return ttlMinutes;
    }


    public Jws<Claims> parseAndValidate(String token){

        return Jwts.parser()
                .verifyWith(key)
                .clockSkewSeconds(Duration.ofSeconds(120).getSeconds())
                .requireIssuer(issuer)
                //.requireAudience(audience) // <- TODO: Lo habilitare luego :D
                .build()
                .parseSignedClaims(token);

    }




}

