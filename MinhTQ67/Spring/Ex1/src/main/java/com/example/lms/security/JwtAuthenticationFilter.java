package com.example.lms.security;

import com.example.lms.service.AccountDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private AccountDetailsService accountDetailsService;

    @Autowired
    private com.example.lms.repository.TokenRepository tokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String accessToken = getAccessTokenFromRequest(request);
            String refreshToken = getCookieValue(request, "refreshToken");

            if (StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
                authenticateWithToken(accessToken, request);
            } else if (StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
                var isRefreshTokenValid = tokenRepository.findByToken(refreshToken)
                        .map(t -> !t.isExpired() && !t.isRevoked()
                                && t.getTokenType() == com.example.lms.entity.Token.TokenType.REFRESH)
                        .orElse(false);

                if (isRefreshTokenValid) {
                    String username = tokenProvider.getUsernameFromJWT(refreshToken);
                    UserDetails userDetails = accountDetailsService.loadUserByUsername(username);
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());

                    String newAccessToken = tokenProvider.generateToken(authentication);
                    com.example.lms.entity.Account user = tokenRepository.findByToken(refreshToken).get().getUser();

                    com.example.lms.entity.Token token = com.example.lms.entity.Token.builder()
                            .user(user)
                            .token(newAccessToken)
                            .tokenType(com.example.lms.entity.Token.TokenType.ACCESS)
                            .expired(false)
                            .revoked(false)
                            .build();
                    tokenRepository.save(token);

                    Cookie accessCookie = new Cookie("accessToken", newAccessToken);
                    accessCookie.setHttpOnly(true);
                    accessCookie.setPath("/");
                    accessCookie.setMaxAge(15 * 60); // 15 minutes
                    response.addCookie(accessCookie);

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        } catch (Exception ex) {
            logger.error("Could not set user authentication in security context", ex);
        }

        filterChain.doFilter(request, response);
    }

    private void authenticateWithToken(String jwt, HttpServletRequest request) {
        var isTokenValid = tokenRepository.findByToken(jwt)
                .map(t -> !t.isExpired() && !t.isRevoked()
                        && t.getTokenType() == com.example.lms.entity.Token.TokenType.ACCESS)
                .orElse(false);

        if (isTokenValid) {
            String username = tokenProvider.getUsernameFromJWT(jwt);

            UserDetails userDetails = accountDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities());
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
    }

    private String getAccessTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return getCookieValue(request, "accessToken");
    }

    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }
}
