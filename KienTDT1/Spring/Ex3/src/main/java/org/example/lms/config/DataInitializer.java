package org.example.lms.config;

import org.example.lms.model.Role;
import org.example.lms.model.User;
import org.example.lms.repository.RoleRepository;
import org.example.lms.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository,
                           UserRepository userRepository,
                           PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed Roles
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("SUPER_ADMIN"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("TEACHER"));
        }

        // Seed Users
        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));

            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setFullName("Super Administrator");
            superAdmin.setStatus("Active");

            Set<Role> roles = new HashSet<>();
            roles.add(superAdminRole);
            superAdmin.setRoles(roles);

            userRepository.save(superAdmin);
        }
    }
}
