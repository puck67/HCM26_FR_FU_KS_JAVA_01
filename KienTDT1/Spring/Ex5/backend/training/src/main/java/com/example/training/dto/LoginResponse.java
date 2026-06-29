package com.example.training.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;

    public LoginResponse(String token) {
        this.token = token;
    }
}