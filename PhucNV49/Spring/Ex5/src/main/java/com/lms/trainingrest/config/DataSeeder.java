package com.lms.trainingrest.config;

import com.lms.trainingrest.entity.Role;
import com.lms.trainingrest.entity.User;
import com.lms.trainingrest.repository.RoleRepository;
import com.lms.trainingrest.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DataSeeder(RoleRepository roleRepository, 
                      UserRepository userRepository, 
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        // Seed default roles if they don't exist
        Role adminRole = getOrCreateRole("ADMIN");
        Role trainerRole = getOrCreateRole("TRAINER");
        Role studentRole = getOrCreateRole("STUDENT");

        // Seed default admin account
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Admin");
            admin.setEmail("admin@lms.com");
            admin.setStatus("ACTIVE");

            Set<Role> roles = new HashSet<>();
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }

    private Role getOrCreateRole(String roleName) {
        return roleRepository.findByRoleName(roleName)
                .orElseGet(() -> {
                    Role role = new Role();
                    role.setRoleName(roleName);
                    return roleRepository.save(role);
                });
    }
}
