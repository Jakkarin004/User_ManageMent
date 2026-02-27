package com.example.usermanagement.GlobalExceptionHandler;

import com.example.usermanagement.DTO.Response.ApiErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    // IllegalArgumentException 400
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponseDTO> handleIllegalArg(
            IllegalArgumentException e) {

        ApiErrorResponseDTO response =
                new ApiErrorResponseDTO(
                        HttpStatus.BAD_REQUEST.value(),
                        e.getMessage()
                );

        return ResponseEntity.badRequest().body(response);
    }

    // Fallback 500 exception อื่นๆ
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponseDTO> handleGeneral(Exception e) {
        ApiErrorResponseDTO response =
                new ApiErrorResponseDTO(
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal server error"
                );

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(response);
    }
}
