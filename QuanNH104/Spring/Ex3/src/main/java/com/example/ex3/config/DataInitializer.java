package com.example.ex3.config;

import com.example.ex3.entity.Role;
import com.example.ex3.entity.User;
import com.example.ex3.repository.RoleRepository;
import com.example.ex3.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Kiểm tra roleRepository.count() == 0
        if (roleRepository.count() == 0) {
            Role superAdminRole = Role.builder().roleName("SUPER_ADMIN").build();
            Role adminRole = Role.builder().roleName("ADMIN").build();
            Role teacherRole = Role.builder().roleName("TEACHER").build();

            roleRepository.save(superAdminRole);
            roleRepository.save(adminRole);
            roleRepository.save(teacherRole);
        }

        // 2. Kiểm tra userRepository.count() == 0
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(superAdminRole);

            User superAdmin = User.builder()
                    .username("superadmin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Super Admin")
                    .status("Active")
                    .roles(roles)
                    .build();

            userRepository.save(superAdmin);
        }
    }
}
