package com.training.security.config;

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
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String redirectUrl = "/dashboard";

        for (GrantedAuthority authority : authorities) {
            String roleName = authority.getAuthority();
            if (roleName.equals("ROLE_SUPER_ADMIN") || roleName.equals("ROLE_ADMIN")) {
                redirectUrl = "/users";
                break;
            }
        }

        response.sendRedirect(request.getContextPath() + redirectUrl);
    }
}
