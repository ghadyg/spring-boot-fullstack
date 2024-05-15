package com.fullStackCourse.jwt;

import com.fullStackCourse.Main;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.sql.Date;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;

@Service
public class JWTUtil {

    private static final String SECRET_KEY =
            "thisismyfirstapp1234_thisismyfirstapp1234_signedbyghady_signedbyghady";

    public String issueToken(String subject)
    {
        return issueToken(subject,Map.of());
    }
    public String issueToken(String subject,String ...scopes)
    {
        return issueToken(subject,Map.of("scopes",scopes));
    }

    public String issueToken(String subject, List<String>scopes)
    {
        return issueToken(subject,Map.of("scopes",scopes));
    }

    public String issueToken(
            String subject,
            Map<String,Object> claims){

        String token = Jwts.builder()
                .claims(claims)
                .subject(subject)
                .issuer("https://demo.com")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(
                        Instant.now()
                                .plus(15, ChronoUnit.DAYS)))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();

        return token;


    }

    public String getSubject(String token){
        Claims payload = getPayload(token);
        return payload.getSubject();


    }

    private Claims getPayload(String token) {
        return Jwts.parser().verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
    public boolean isTokenValid(String jwt,String username)
    {
        String subject = getSubject(jwt);
        return subject.equals(username) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return getPayload(jwt).getExpiration().before(java.util.Date.from(Instant.now()));

    }

    private SecretKey getSigningKey()
    {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }
}
