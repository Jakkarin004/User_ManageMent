package com.example.usermanagement.Utils;

import org.springframework.http.ResponseCookie;

public class CookieUtil {

    private static final String REFRESH_TOKEN_NAME = "refresh_token";

    public static ResponseCookie createRefreshTokenCookie(String refreshToken) {
        return ResponseCookie.from(REFRESH_TOKEN_NAME,refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/user")
                .build();
    }
}
