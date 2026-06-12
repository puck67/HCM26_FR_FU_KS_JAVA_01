package com.example.ex3.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        
        boolean isSuperAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("SUPER_ADMIN"));
        boolean isAdmin = authorities.stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
        boolean isTeacher = authorities.stream().anyMatch(a -> a.getAuthority().equals("TEACHER"));

        if (isSuperAdmin || isAdmin) {
            response.sendRedirect("/users");
        } else if (isTeacher) {
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
}
