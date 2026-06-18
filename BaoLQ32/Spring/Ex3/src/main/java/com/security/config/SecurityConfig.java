package com.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.Set;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            // Configure CSRF - bypass for H2 Console testing
            .csrf(csrf -> csrf
                .ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**"))
            )
            // Configure Frame Options - required to load H2 Console in frame
            .headers(headers -> headers
                .frameOptions(frame -> frame.disable())
            )
            // Path Authorization Rules
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    new AntPathRequestMatcher("/login"),
                    new AntPathRequestMatcher("/css/**"),
                    new AntPathRequestMatcher("/js/**"),
                    new AntPathRequestMatcher("/h2-console/**")
                ).permitAll()
                .requestMatchers(new AntPathRequestMatcher("/users/**")).hasAnyRole("SUPER_ADMIN", "ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/roles/**")).hasRole("SUPER_ADMIN")
                .requestMatchers(new AntPathRequestMatcher("/dashboard")).authenticated()
                .anyRequest().authenticated()
            )
            // Form Login Configuration
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                // Success Handler: Redirect users depending on their Role (Bonus 1)
                .successHandler((request, response, authentication) -> {
                    Set<String> roles = AuthorityUtils.authorityListToSet(authentication.getAuthorities());
                    if (roles.contains("ROLE_SUPER_ADMIN") || roles.contains("ROLE_ADMIN")) {
                        response.sendRedirect("/users");
                    } else if (roles.contains("ROLE_TEACHER")) {
                        response.sendRedirect("/dashboard");
                    } else {
                        response.sendRedirect("/dashboard");
                    }
                })
                .failureUrl("/login?error=true")
                .permitAll()
            )
            // Logout Configuration
            .logout(logout -> logout
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
            );

        return http.build();
    }
}
