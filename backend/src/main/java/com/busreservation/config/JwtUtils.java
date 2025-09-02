package com.busreservation.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Date;

/**
 * Utility to create/validate JWTs. Uses a key built from app.jwtSecret.
 */
@Component
public class JwtUtils {

    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs:3600000}")
    private long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] keyBytes = jwtSecret.getBytes(StandardCharsets.UTF_8);
        if (keyBytes.length < 32) {
            try {
                // deterministic expansion to at least 32 bytes
                MessageDigest sha = MessageDigest.getInstance("SHA-256");
                keyBytes = sha.digest(keyBytes);
            } catch (NoSuchAlgorithmException e) {
                keyBytes = Arrays.copyOf(keyBytes, 32);
            }
        }
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /**
     * Generate a JWT for the given user details.
     */
    public String generateToken(UserDetails userDetails) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + jwtExpirationMs);
        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Extract username from token.
     */
    public String getUsernameFromJwt(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Basic validation: parse with signing key.
     */
    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException ex) {
            return false;
        }
    }

    /**
     * Build an Authentication for the supplied UserDetails (username/password not used).
     */
    public org.springframework.security.core.Authentication getAuthentication(String token, UserDetails userDetails) {
        return new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
