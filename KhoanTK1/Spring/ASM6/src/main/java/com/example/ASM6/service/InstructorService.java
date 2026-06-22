package com.example.ASM6.service;

import com.example.ASM6.model.Instructor;
import com.example.ASM6.repository.InstructorRepository;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class InstructorService {

    private final InstructorRepository instructorRepo;

    public InstructorService(InstructorRepository instructorRepo) {
        this.instructorRepo = instructorRepo;
    }

    public Optional<Instructor> login(String username, String password) {
        Optional<Instructor> match = instructorRepo.findByUsername(username);
        if (match.isPresent() && match.get().getPassword().equals(password)) {
            return match;
        }
        return Optional.empty();
    }

    public void seedDefaultInstructor() {
        if (instructorRepo.count() == 0) {
            instructorRepo.save(Instructor.builder()
                    .username("admin")
                    .password("admin")
                    .build());
        }
    }
}
