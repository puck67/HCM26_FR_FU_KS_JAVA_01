package com.example.ex3.bootstrap;

import com.example.ex3.entity.Role;
import com.example.ex3.entity.User;
import com.example.ex3.entity.UserStatus;
import com.example.ex3.repository.RoleRepository;
import com.example.ex3.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("SUPER_ADMIN"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("TEACHER"));
            System.out.println("Initialized default roles.");
        }

        if (userRepository.count() == 0) {
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("admin123"));
            superAdmin.setFullName("System Super Administrator");
            superAdmin.setStatus(UserStatus.ACTIVE);

            Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN")
                    .orElseThrow(() -> new RuntimeException("SUPER_ADMIN role not found"));
            superAdmin.getRoles().add(superAdminRole);

            userRepository.save(superAdmin);
            System.out.println("Initialized superadmin account.");
        }
    }
}
