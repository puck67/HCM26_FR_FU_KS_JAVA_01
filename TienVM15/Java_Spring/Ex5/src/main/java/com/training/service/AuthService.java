package com.training.service;

import com.training.dto.LoginRequest;
import com.training.dto.LoginResponse;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    LoginResponse refreshToken(String refreshToken);
}
