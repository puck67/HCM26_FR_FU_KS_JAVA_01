package com.example.ex4.config;

import com.example.ex4.entity.Subject;
import com.example.ex4.repository.SubjectRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private final SubjectRepository subjectRepository;

    public DatabaseSeeder(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (subjectRepository.count() == 0) {
            subjectRepository.saveAll(List.of(
                    new Subject("JAVA01", "Java Programming", 40),
                    new Subject("SPRING01", "Spring Framework", 60),
                    new Subject("DB01", "Database Design", 30)
            ));
            System.out.println("Default subjects created.");
        }
    }
}
