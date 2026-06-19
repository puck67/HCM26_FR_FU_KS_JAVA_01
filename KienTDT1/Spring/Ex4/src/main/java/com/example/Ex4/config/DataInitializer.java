package com.example.Ex4.config;

import com.example.Ex4.entity.Subject;
import com.example.Ex4.repository.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(SubjectRepository subjectRepository) {
        return args -> {
            if (subjectRepository.count() == 0) {
                Subject java = new Subject();
                java.setSubjectCode("JAVA01");
                java.setSubjectName("Java Programming");
                java.setDuration(40);

                Subject spring = new Subject();
                spring.setSubjectCode("SPRING01");
                spring.setSubjectName("Spring Framework");
                spring.setDuration(50);

                Subject db = new Subject();
                db.setSubjectCode("DB01");
                db.setSubjectName("Database Design");
                db.setDuration(30);

                subjectRepository.save(java);
                subjectRepository.save(spring);
                subjectRepository.save(db);
            }
        };
    }
}