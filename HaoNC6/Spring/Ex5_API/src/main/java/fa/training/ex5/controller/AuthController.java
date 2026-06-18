package fa.training.ex5.controller;

import fa.training.ex5.dto.response.ApiResponse;
import fa.training.ex5.dto.response.AuthResponse;
import fa.training.ex5.dto.request.LoginRequest;
import fa.training.ex5.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        AuthResponse authResponse = authService.login(request);
        return ResponseEntity
                .ok(ApiResponse.success("Login successful", authResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refresh(
            @Valid @RequestBody fa.training.ex5.dto.request.TokenRefreshRequest request) {
        AuthResponse authResponse = authService.refresh(request);
        return ResponseEntity
                .ok(ApiResponse.success("Token refreshed successfully", authResponse));
    }
}
