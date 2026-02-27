package com.example.usermanagement.Controller;

import com.example.usermanagement.DTO.Request.LoginRequestDTO;
import com.example.usermanagement.DTO.Request.UserRequestDTO;
import com.example.usermanagement.DTO.Response.LoginResponDTO;
import com.example.usermanagement.Service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@Valid @RequestBody UserRequestDTO request){
        authService.register(request);
        return "Register Success";
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponDTO> login(@Valid @RequestBody LoginRequestDTO request,
                                                HttpServletResponse response){
        LoginResponDTO loginResponDTO = authService.login(request,response);
        return ResponseEntity.ok(loginResponDTO);
    }


}
