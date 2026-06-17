package com.training.config;

import com.training.entity.Course;
import com.training.entity.Role;
import com.training.entity.User;
import com.training.repository.CourseRepository;
import com.training.repository.RoleRepository;
import com.training.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class TrainingDataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CourseRepository courseRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Roles
        if (roleRepository.count() == 0) {
            roleRepository.save(Role.builder().name("ADMIN").build());
            roleRepository.save(Role.builder().name("TRAINER").build());
            roleRepository.save(Role.builder().name("STUDENT").build());
        }

        // 2. Seed default admin user
        if (userRepository.count() == 0) {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseThrow(() -> new IllegalStateException("ADMIN role not found after seeding"));

            User admin = User.builder()
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .fullName("System Admin")
                    .email("admin@training.com")
                    .status("ACTIVE")
                    .roles(Collections.singleton(adminRole))
                    .build();

            userRepository.save(admin);
        }

        // 3. Seed some initial courses
        if (courseRepository.count() == 0) {
            courseRepository.save(Course.builder()
                    .courseName("Java Core Fundamentals")
                    .duration(40)
                    .description("Learn basic Java syntax, OOP principles, and Collections framework.")
                    .build());

            courseRepository.save(Course.builder()
                    .courseName("Spring Boot Enterprise")
                    .duration(60)
                    .description("Build scalable Restful APIs using Spring Data JPA, MVC, and Security.")
                    .build());

            courseRepository.save(Course.builder()
                    .courseName("Relational Database Design")
                    .duration(30)
                    .description("Understand relational modeling, SQL queries, indexing, and transactions.")
                    .build());
        }
    }
}
