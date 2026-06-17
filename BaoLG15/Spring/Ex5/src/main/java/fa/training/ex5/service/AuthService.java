package fa.training.ex5.service;


import fa.training.ex5.dto.response.AuthResponse;
import fa.training.ex5.dto.request.LoginRequest;

public interface AuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(fa.training.ex5.dto.request.TokenRefreshRequest request);
}
