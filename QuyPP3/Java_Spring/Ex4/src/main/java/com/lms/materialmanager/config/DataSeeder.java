package com.lms.materialmanager.config;

import com.lms.materialmanager.entity.Subject;
import com.lms.materialmanager.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SubjectRepository subjectRepository;

    public DataSeeder(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (subjectRepository.count() == 0) {
            // Seed Subject 1: JAVA01 Java Programming
            Subject javaSub = new Subject()
                    .setSubjectCode("JAVA01")
                    .setSubjectName("Java Programming")
                    .setDuration(40);
            subjectRepository.save(javaSub);

            // Seed Subject 2: SPRING01 Spring Framework
            Subject springSub = new Subject()
                    .setSubjectCode("SPRING01")
                    .setSubjectName("Spring Framework")
                    .setDuration(45);
            subjectRepository.save(springSub);

            // Seed Subject 3: DB01 Database Design
            Subject dbSub = new Subject()
                    .setSubjectCode("DB01")
                    .setSubjectName("Database Design")
                    .setDuration(30);
            subjectRepository.save(dbSub);
        }
    }
}
