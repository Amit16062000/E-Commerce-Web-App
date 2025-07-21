package com.example.ecommerce.Config;

import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${jwt.secret:mySecretKey123456789012345678901234567890123456789012345678901234567890}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private long expiration;

    @Bean
    public SecretKey secretKey() {
        // Ensure the secret is properly encoded
        if (secret.length() < 64) {
            throw new IllegalArgumentException("JWT secret must be at least 64 characters long for HS512");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public long getExpiration() {
        return expiration;
    }

}
