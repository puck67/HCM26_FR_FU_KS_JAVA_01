package com.lms.trainingrest.controller;

import com.lms.trainingrest.dto.LoginRequest;
import com.lms.trainingrest.dto.LoginResponse;
import com.lms.trainingrest.dto.TokenRefreshRequestDTO;
import com.lms.trainingrest.dto.TokenRefreshResponseDTO;
import com.lms.trainingrest.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "Endpoints for login and token refresh")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Authenticate user credentials and obtain JWT tokens")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse response = authService.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/refresh")
    @Operation(summary = "Regenerate access token using a valid refresh token")
    public ResponseEntity<TokenRefreshResponseDTO> refresh(@Valid @RequestBody TokenRefreshRequestDTO refreshRequest) {
        TokenRefreshResponseDTO response = authService.refreshToken(refreshRequest);
        return ResponseEntity.ok(response);
    }
}
