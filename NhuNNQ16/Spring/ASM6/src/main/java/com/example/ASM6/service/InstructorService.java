package com.example.ASM6.service;

import com.example.ASM6.model.Instructor;
import com.example.ASM6.repository.InstructorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InstructorService {

    private final InstructorRepository instructorRepository;

    public Optional<Instructor> login(String username, String password) {
        return instructorRepository.findByUsername(username)
                .filter(instructor -> instructor.getPassword().equals(password));
    }

    public void seedDefaultInstructor() {
        if (instructorRepository.count() == 0) {
            instructorRepository.save(Instructor.builder()
                    .username("admin")
                    .password("admin")
                    .build());
        }
    }
}
