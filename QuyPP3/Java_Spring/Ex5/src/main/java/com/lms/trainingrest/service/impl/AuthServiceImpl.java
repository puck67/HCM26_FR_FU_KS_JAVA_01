package com.lms.trainingrest.service.impl;

import com.lms.trainingrest.dto.LoginRequest;
import com.lms.trainingrest.dto.LoginResponse;
import com.lms.trainingrest.dto.TokenRefreshRequestDTO;
import com.lms.trainingrest.dto.TokenRefreshResponseDTO;
import com.lms.trainingrest.entity.RefreshToken;
import com.lms.trainingrest.entity.User;
import com.lms.trainingrest.repository.RefreshTokenRepository;
import com.lms.trainingrest.repository.UserRepository;
import com.lms.trainingrest.security.JwtTokenProvider;
import com.lms.trainingrest.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${app.jwt.refresh-expiration-milliseconds}")
    private long refreshExpirationInMs;

    @Autowired
    public AuthServiceImpl(AuthenticationManager authenticationManager, 
                            JwtTokenProvider tokenProvider, 
                            RefreshTokenRepository refreshTokenRepository, 
                            UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = tokenProvider.generateToken(authentication);

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + loginRequest.getUsername()));

        // Delete any existing refresh token for this user
        refreshTokenRepository.deleteByUser(user);
        refreshTokenRepository.flush();

        // Generate and save new refresh token
        RefreshToken refreshToken = createRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken.getToken());
    }

    @Override
    @Transactional
    public TokenRefreshResponseDTO refreshToken(TokenRefreshRequestDTO refreshRequest) {
        String requestToken = refreshRequest.getRefreshToken();

        RefreshToken refreshToken = refreshTokenRepository.findByToken(requestToken)
                .orElseThrow(() -> new IllegalArgumentException("Unauthorized")); // Maps to 401 Unauthorized via handler

        // Verify expiry
        if (refreshToken.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new IllegalArgumentException("Unauthorized"); // Expired refresh token
        }

        User user = refreshToken.getUser();
        String newAccessToken = tokenProvider.generateToken(user.getUsername());

        // Rotate the refresh token for extra security
        refreshTokenRepository.delete(refreshToken);
        refreshTokenRepository.flush();
        RefreshToken newRefreshToken = createRefreshToken(user);

        return new TokenRefreshResponseDTO(newAccessToken, newRefreshToken.getToken());
    }

    private RefreshToken createRefreshToken(User user) {
        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshExpirationInMs));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }
}
