package com.lms.securitymanager.config;

import com.lms.securitymanager.entity.Role;
import com.lms.securitymanager.entity.User;
import com.lms.securitymanager.repository.RoleRepository;
import com.lms.securitymanager.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseInitializer(RoleRepository roleRepository,
                               UserRepository userRepository,
                               PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Roles
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("SUPER_ADMIN"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("TEACHER"));
        }

        // 2. Seed Super Admin User
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseThrow(() -> new IllegalStateException("SUPER_ADMIN role not found in database"));

            Set<Role> roles = new HashSet<>();
            roles.add(superAdminRole);

            User superAdmin = new User()
                    .setUsername("superadmin")
                    .setPassword(passwordEncoder.encode("admin123"))
                    .setFullName("Super Administrator")
                    .setStatus(true)
                    .setRoles(roles);

            userRepository.save(superAdmin);
        }
    }
}
