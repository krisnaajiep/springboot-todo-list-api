package com.krisnaajiep.todolistapi.security;

/*
IntelliJ IDEA 2025.1 (Ultimate Edition)
Build #IU-251.23774.435, built on April 14, 2025
@Author krisna a.k.a. Krisna Ajie
Java Developer
Created on 07/05/25 09.32
@Last Modified 07/05/25 09.32
Version 1.0
*/

import com.krisnaajiep.todolistapi.config.JwtConfig;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.UUID;

@Component
public class JwtUtil {
    private final SecretKey key;
    private final long accessTokenExpiration;
    private final long refreshTokenExpiration;

    public JwtUtil(JwtConfig jwtConfig) {
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtConfig.getSecret()));
        this.accessTokenExpiration = jwtConfig.getAccessTokenExpiration();
        this.refreshTokenExpiration = jwtConfig.getRefreshTokenExpiration();
    }

    public String generateToken(String subject, String purpose, String familyId) {
        long expiration = purpose.equals("refresh")
                ? refreshTokenExpiration
                : accessTokenExpiration;

        JwtBuilder jwtBuilder = Jwts.builder()
                .id(UUID.randomUUID().toString())
                .subject(subject)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .claim("purpose", purpose)
                .claim("familyId", familyId);

        return jwtBuilder.signWith(key).compact();
    }

    public String extractSubject(String token) throws Exception {
        return getClaimsJws(token)
                .getPayload()
                .getSubject();
    }

    public String extractPurpose(String token) throws Exception {
        return getClaimsJws(token)
                .getPayload()
                .get("purpose")
                .toString();
    }

    public String extractFamilyId(String token) throws Exception {
        return getClaimsJws(token)
                .getPayload()
                .get("familyId")
                .toString();
    }

    public boolean isTokenExpired(String token) throws Exception {
        Date expiration = getClaimsJws(token)
                .getPayload()
                .getExpiration();

        return expiration.before(new Date());
    }

    public boolean isTokenPairValid(String accessToken, String refreshToken) throws Exception {
        return extractPurpose(accessToken).equals("access") &&
                extractPurpose(refreshToken).equals("refresh") &&
                extractFamilyId(accessToken).equals(extractFamilyId(refreshToken));
    }

    private Jws<Claims> getClaimsJws(String token) throws Exception {
        try {

            return Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
        } catch (JwtException | IllegalArgumentException e) {
            throw new Exception("Unauthorized");
        }
    }
}
