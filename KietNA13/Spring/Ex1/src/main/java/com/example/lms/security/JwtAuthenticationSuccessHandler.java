package com.example.lms.security;

import com.example.lms.entity.Account;
import com.example.lms.entity.Token;
import com.example.lms.repository.AccountRepository;
import com.example.lms.repository.TokenRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;
    private final TokenRepository tokenRepository;
    private final AccountRepository accountRepository;

    public JwtAuthenticationSuccessHandler(JwtTokenProvider tokenProvider,
                                           TokenRepository tokenRepository,
                                           AccountRepository accountRepository) {
        this.tokenProvider = tokenProvider;
        this.tokenRepository = tokenRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {
        String accessToken = tokenProvider.generateToken(authentication);
        String refreshToken = tokenProvider.generateRefreshToken(authentication);

        Account user = accountRepository.findByUsername(authentication.getName()).orElse(null);
        if (user != null) {
            revokeAllUserTokens(user);
            saveUserToken(user, accessToken, com.example.lms.entity.Token.TokenType.ACCESS);
            saveUserToken(user, refreshToken, com.example.lms.entity.Token.TokenType.REFRESH);
        }

        Cookie accessCookie = new Cookie("accessToken", accessToken);
        accessCookie.setHttpOnly(true);
        accessCookie.setPath("/");
        accessCookie.setMaxAge(15 * 60); // 15 minutes

        Cookie refreshCookie = new Cookie("refreshToken", refreshToken);
        refreshCookie.setHttpOnly(true);
        refreshCookie.setPath("/");
        refreshCookie.setMaxAge(7 * 24 * 60 * 60); // 7 days

        response.addCookie(accessCookie);
        response.addCookie(refreshCookie);

        super.onAuthenticationSuccess(request, response, authentication);
    }

    private void saveUserToken(Account user, String jwtToken, com.example.lms.entity.Token.TokenType type) {
        Token token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(type)
                .expired(false)
                .revoked(false)
                .build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(Account user) {
        var validUserTokens = tokenRepository.findAllValidTokensByUser(user.getId());
        if (validUserTokens.isEmpty())
            return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }
}
