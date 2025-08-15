package com.example.Bank_REST.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;

@Component
public class JwtUtil {
    
    @Value("${security.jwt.secrete}")
    private String secrete;

    private SecretKey secretKey;

    private final Long EXPIRATION_TIME = 86400000L;

    @PostConstruct
    void init() {
        secretKey = generateSecretKey(secrete);
    }

    public String generateToken(UserDetails details) {
        return Jwts.builder()
            .subject(details.getUsername())
            .claim("role", details.getAuthorities())
            .issuedAt(new Date())
            .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .signWith(secretKey, Jwts.SIG.HS256)
            .compact();
    }

    private SecretKey generateSecretKey(String secrete) {
        return Keys.hmacShaKeyFor(secrete.getBytes());
    }

    public String getUserName(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getSubject();
    }

    public boolean validateToken(String token, UserDetails details) {
        return getUserName(token).equals(details.getUsername()) && !tokenExpired(token);
    }

    public boolean tokenExpired(String token) {
        return Jwts.parser()
            .verifyWith(secretKey)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }
}
