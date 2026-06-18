package com.material.config;

import com.material.entity.Subject;
import com.material.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MaterialDataInitializer implements CommandLineRunner {

    private final SubjectRepository subjectRepository;

    @Override
    public void run(String... args) throws Exception {
        if (subjectRepository.count() > 0) {
            return;
        }

        subjectRepository.save(Subject.builder()
                .code("JAVA01")
                .name("Java Programming")
                .duration(40)
                .build());

        subjectRepository.save(Subject.builder()
                .code("SPRING01")
                .name("Spring Framework")
                .duration(60)
                .build());

        subjectRepository.save(Subject.builder()
                .code("DB01")
                .name("Database Design")
                .duration(30)
                .build());
    }
}
