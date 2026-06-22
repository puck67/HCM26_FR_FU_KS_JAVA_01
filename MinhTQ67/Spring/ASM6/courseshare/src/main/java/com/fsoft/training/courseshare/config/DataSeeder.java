package com.fsoft.training.courseshare.config;

import com.fsoft.training.courseshare.entity.Instructor;
import com.fsoft.training.courseshare.repository.InstructorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataSeeder {

    @Bean
    public CommandLineRunner initData(InstructorRepository instructorRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (instructorRepository.count() == 0) {
                Instructor instructor = new Instructor();
                instructor.setUsername("admin");
                instructor.setPassword(passwordEncoder.encode("admin"));
                instructorRepository.save(instructor);
            }
        };
    }
}
