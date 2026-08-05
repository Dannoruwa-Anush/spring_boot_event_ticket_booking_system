package com.example.demo.security.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

    @Value("${jwt.secret}") // gets value of variable defined in the resources/application.properties
    private String jwtSecret;

    @Value("${jwt.expiration}") // gets value of variable defined in the resources/application.properties
    private long jwtExpirationMs;

    // Password change token expiry (10 minutes)
    private static final long PASSWORD_CHANGE_TOKEN_EXPIRATION = 10 * 60 * 1000;

    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generatePasswordChangeToken(UserDetails user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("type", "PASSWORD_CHANGE")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + PASSWORD_CHANGE_TOKEN_EXPIRATION))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    public String generatePasswordResetToken(UserDetails user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("type", "PASSWORD_RESET")
                .issuedAt(new Date())
                .expiration(
                        new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .signWith(getSigningKey(), Jwts.SIG.HS256)
                .compact();
    }

    private Claims extractClaims(String token) {

        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUsername(String token) {

        return extractClaims(token).getSubject();
    }

    public String extractTokenType(String token) {

        return extractClaims(token).get("type", String.class);
    }

    private Date extractExpiration(String token) {

        return extractClaims(token).getExpiration();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {

        String username = extractUsername(token);

        return username.equals(userDetails.getUsername())
                && !isTokenExpired(token);
    }

    public boolean isPasswordChangeToken(String token) {

        String type = extractTokenType(token);

        return "PASSWORD_CHANGE".equals(type)
                && !isTokenExpired(token);
    }

    public boolean isPasswordResetToken(String token) {

        String type = extractClaims(token)
                .get("type", String.class);

        return "PASSWORD_RESET".equals(type)
                && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {

        return extractExpiration(token).before(new Date());
    }
}
