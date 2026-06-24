package com.lms.trainingrest.service;

import com.lms.trainingrest.dto.LoginRequest;
import com.lms.trainingrest.dto.LoginResponse;
import com.lms.trainingrest.dto.TokenRefreshRequestDTO;
import com.lms.trainingrest.dto.TokenRefreshResponseDTO;

public interface AuthService {
    LoginResponse login(LoginRequest loginRequest);
    TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO refreshRequest);
}
