package com.example.usermanagement.Service;

import com.example.usermanagement.Config.JwtConfig;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RefreshTokenService {

    private static final String KEY_REFRESH_TOKEN = "refresh";
    private final RedisTemplate<String, String> redisTemplate;

    private final JwtConfig jwtConfig;

    public RefreshTokenService(JwtConfig jwtConfig, RedisTemplate<String, String> redisTemplate) {
        this.jwtConfig = jwtConfig;
        this.redisTemplate = redisTemplate;
    }

    public void save(String username, String refreshToken) {

        try {
            redisTemplate.opsForValue()
                    .set(
                            KEY_REFRESH_TOKEN + username,
                            refreshToken,
                            jwtConfig.getRefreshExpiration() / 1000,
                            TimeUnit.SECONDS
                    );
        } catch (Exception e) {
//            e.printStackTrace();
            throw new RuntimeException("Error while saving refresh token", e);
        }
    }




}
