package com.example.ex3.config;

import com.example.ex3.entity.Role;
import com.example.ex3.entity.User;
import com.example.ex3.repository.RoleRepository;
import com.example.ex3.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DatabaseSeeder(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("SUPER_ADMIN"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("TEACHER"));
            System.out.println("Default roles created.");
        }

        if (userRepository.count() == 0) {
            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("Role SUPER_ADMIN not found"));

            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setFullName("Super Administrator");
            superAdmin.setStatus("ACTIVE");
            superAdmin.setRoles(Set.of(superAdminRole));

            userRepository.save(superAdmin);
            System.out.println("Super admin user created.");
        }
    }
}
