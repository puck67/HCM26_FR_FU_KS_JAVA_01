package com.lms.materialmanager.config;

import com.lms.materialmanager.entity.Subject;
import com.lms.materialmanager.repository.SubjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SubjectRepository subjectRepository;

    @Autowired
    public DataSeeder(SubjectRepository subjectRepository) {
        this.subjectRepository = subjectRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (subjectRepository.count() == 0) {
            // Seed Subject 1: JAVA01 Java Programming
            Subject javaSub = new Subject();
            javaSub.setSubjectCode("JAVA01");
            javaSub.setSubjectName("Java Programming");
            javaSub.setDuration(40);
            subjectRepository.save(javaSub);

            // Seed Subject 2: SPRING01 Spring Framework
            Subject springSub = new Subject();
            springSub.setSubjectCode("SPRING01");
            springSub.setSubjectName("Spring Framework");
            springSub.setDuration(45);
            subjectRepository.save(springSub);

            // Seed Subject 3: DB01 Database Design
            Subject dbSub = new Subject();
            dbSub.setSubjectCode("DB01");
            dbSub.setSubjectName("Database Design");
            dbSub.setDuration(30);
            subjectRepository.save(dbSub);
        }
    }
}
