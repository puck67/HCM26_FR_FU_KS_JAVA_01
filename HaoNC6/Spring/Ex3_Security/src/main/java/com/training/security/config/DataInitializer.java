package com.training.security.config;

import com.training.security.entity.Role;
import com.training.security.entity.User;
import com.training.security.repository.RoleRepository;
import com.training.security.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // 1. Seed Roles if empty
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().roleName("SUPER_ADMIN").build());
            roleRepository.save(Role.builder().roleName("ADMIN").build());
            roleRepository.save(Role.builder().roleName("TEACHER").build());
            System.out.println("Default roles seeded: SUPER_ADMIN, ADMIN, TEACHER");
        }

        // 2. Seed Super Admin User if empty
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

            Set<Role> roles = new HashSet<>();
            roles.add(superAdminRole);

            User superAdmin = User.builder()
                    .username("superadmin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("Super Admin")
                    .status("ACTIVE")
                    .roles(roles)
                    .build();

            userRepository.save(superAdmin);
            System.out.println("Default Super Admin seeded: username='superadmin', password='admin123'");
        }
    }
}
