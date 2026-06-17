package com.security.config;

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
public class CustomSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        boolean isAdminOrSuper = false;
        boolean isTeacher = false;

        for (GrantedAuthority authority : authorities) {
            String roleName = authority.getAuthority();
            if ("ROLE_SUPER_ADMIN".equals(roleName) || "ROLE_ADMIN".equals(roleName)) {
                isAdminOrSuper = true;
                break;
            } else if ("ROLE_TEACHER".equals(roleName)) {
                isTeacher = true;
            }
        }

        if (isAdminOrSuper) {
            response.sendRedirect("/users");
        } else if (isTeacher) {
            response.sendRedirect("/dashboard");
        } else {
            response.sendRedirect("/dashboard");
        }
    }
}
