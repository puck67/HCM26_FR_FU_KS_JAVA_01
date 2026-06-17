package com.security.config;

import com.security.entity.Role;
import com.security.entity.User;
import com.security.repository.RoleRepository;
import com.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class SecurityDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Roles
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name("SUPER_ADMIN").build());
            roleRepository.save(Role.builder().name("ADMIN").build());
            roleRepository.save(Role.builder().name("TEACHER").build());
        }

        // 2. Seed Super Admin User
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByName("SUPER_ADMIN")
                    .orElseThrow(() -> new IllegalStateException("SUPER_ADMIN role not found after seeding"));

            User superAdmin = User.builder()
                    .username("superadmin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Super Admin")
                    .status("ACTIVE")
                    .roles(Collections.singleton(superAdminRole))
                    .build();

            userRepository.save(superAdmin);
        }
    }
}
