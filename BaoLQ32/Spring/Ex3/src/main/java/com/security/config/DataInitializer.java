package com.security.config;

import com.security.model.Role;
import com.security.model.User;
import com.security.repository.RoleRepository;
import com.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Roles if table is empty
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("SUPER_ADMIN"));
            roleRepository.save(new Role("ADMIN"));
            roleRepository.save(new Role("TEACHER"));
        }

        // Get Role instances
        Role superAdminRole = roleRepository.findByRoleName("SUPER_ADMIN").orElse(null);
        Role adminRole = roleRepository.findByRoleName("ADMIN").orElse(null);
        Role teacherRole = roleRepository.findByRoleName("TEACHER").orElse(null);

        // 2. Seed Users if table is empty
        if (userRepository.count() == 0) {
            // A. Super Admin account (Mandatory bootstrap account)
            User superAdmin = new User();
            superAdmin.setUsername("superadmin");
            superAdmin.setPassword(passwordEncoder.encode("admin123")); // Hashed!
            superAdmin.setFullName("Super Administrator");
            superAdmin.setStatus("ACTIVE");
            
            Set<Role> superRoles = new HashSet<>();
            if (superAdminRole != null) superRoles.add(superAdminRole);
            superAdmin.setRoles(superRoles);
            userRepository.save(superAdmin);

            // B. Admin account (For testing ADMIN authorization)
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setFullName("System Administrator");
            admin.setStatus("ACTIVE");
            
            Set<Role> adminRoles = new HashSet<>();
            if (adminRole != null) adminRoles.add(adminRole);
            admin.setRoles(adminRoles);
            userRepository.save(admin);

            // C. Teacher account (For testing TEACHER authorization)
            User teacher = new User();
            teacher.setUsername("teacher");
            teacher.setPassword(passwordEncoder.encode("admin123"));
            teacher.setFullName("Professor Smith");
            teacher.setStatus("ACTIVE");
            
            Set<Role> teacherRoles = new HashSet<>();
            if (teacherRole != null) teacherRoles.add(teacherRole);
            teacher.setRoles(teacherRoles);
            userRepository.save(teacher);

            // D. Inactive Teacher account (For testing Lockout / INACTIVE login check)
            User inactiveUser = new User();
            inactiveUser.setUsername("inactiveuser");
            inactiveUser.setPassword(passwordEncoder.encode("admin123"));
            inactiveUser.setFullName("Disabled Instructor");
            inactiveUser.setStatus("INACTIVE"); // Locked out!
            
            Set<Role> inactiveRoles = new HashSet<>();
            if (teacherRole != null) inactiveRoles.add(teacherRole);
            inactiveUser.setRoles(inactiveRoles);
            userRepository.save(inactiveUser);
        }
    }
}
