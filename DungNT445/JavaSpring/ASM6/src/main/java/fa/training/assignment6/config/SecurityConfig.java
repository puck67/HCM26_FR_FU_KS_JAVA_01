package fa.training.assignment6.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/js/**", "/images/**", "/public/**").permitAll()
                .requestMatchers("/", "/course/**", "/category/**").permitAll()
                .requestMatchers("/instructor/login").permitAll()
                .requestMatchers("/instructor/**").authenticated()
                .requestMatchers("/api/instructor/**").authenticated()
                .anyRequest().permitAll()
            )
            .formLogin(form -> form
                .loginPage("/instructor/login")
                .loginProcessingUrl("/instructor/login-process")
                .defaultSuccessUrl("/instructor/dashboard", true)
                .failureUrl("/instructor/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/instructor/logout")
                .logoutSuccessUrl("/instructor/login?logout=true")
                .permitAll()
            );

        return http.build();
    }
}
