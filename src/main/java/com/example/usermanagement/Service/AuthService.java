package com.example.usermanagement.Service;

import com.example.usermanagement.DTO.Request.LoginRequestDTO;
import com.example.usermanagement.DTO.Request.UserRequestDTO;
import com.example.usermanagement.DTO.Response.AccessTokenResponseDTO;
import com.example.usermanagement.DTO.Response.LoginResponDTO;
import com.example.usermanagement.Entity.UserEntity;
import com.example.usermanagement.Repository.UserRepository;
import com.example.usermanagement.Utils.CookieUtil;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;


    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, RefreshTokenService refreshTokenService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.refreshTokenService = refreshTokenService;
    }

    public void register(UserRequestDTO request){
        try {
            UserEntity user = new UserEntity();
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setPhone(request.getPhone());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
            user.setCreatedAt(new Date());

            userRepository.save(user);

        }catch (Exception e){
            throw new RuntimeException("server error",e);
        }
    }

    public LoginResponDTO login(LoginRequestDTO request, HttpServletResponse response){

        UserEntity user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        // สร้าง accessToken
        String accessToken = jwtService.generateAccessToken(user.getUsername(), user.getRole());

        // สร้าง refreshToken
        String refreshToken = jwtService.generateRefreshToken(user.getUsername());
        refreshTokenService.save(user.getUsername(),refreshToken);

        // สร้าง cookie สำหรับ Refresh Token
        response.setHeader("Set-Cookie", CookieUtil.createRefreshTokenCookie(refreshToken).toString());

        // สร้าง Response DTO
        LoginResponDTO loginResponse = new LoginResponDTO();
        loginResponse.setAccessToken(accessToken);
        loginResponse.setId(user.getId().toString());
        loginResponse.setUsername(user.getUsername());
        loginResponse.setEmail(user.getEmail());
        loginResponse.setPhone(user.getPhone());
        loginResponse.setRole(user.getRole());
        return loginResponse;
    }

    public AccessTokenResponseDTO refreshAccessToken(String refreshToken) {

        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new RuntimeException("Refresh token missing");
        }

        String username = jwtService.extractUsername(refreshToken);

        if (!refreshTokenService.validate(username, refreshToken)) {
            throw new RuntimeException("Invalid refresh token");
        }

        UserEntity user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String newAccessToken =
                jwtService.generateAccessToken(user.getUsername(), user.getRole());

        AccessTokenResponseDTO response = new AccessTokenResponseDTO();
        response.setAccessToken(newAccessToken);
        return response;
    }
}
