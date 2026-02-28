package com.example.usermanagement.Service;

import com.example.usermanagement.Config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class jwtService {

    private final JwtConfig jwtConfig;

    public jwtService(JwtConfig jwtConfig){
        this.jwtConfig = jwtConfig;
    }

    public String generateAccessToken(String username, String role) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .claim("role", role)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getExpiration()))
                    .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                    .compact();
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("JWT secret key is invalid", e);
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    public String generateRefreshToken(String username) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date())
                    .setExpiration(new Date(System.currentTimeMillis() + jwtConfig.getRefreshExpiration()))
                    .signWith(Keys.hmacShaKeyFor(jwtConfig.getSecret().getBytes()))
                    .compact();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate refresh token", e);
        }
    }
}
