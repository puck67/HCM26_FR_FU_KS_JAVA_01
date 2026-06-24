package com.example.demo.service;

import com.example.demo.model.Instructor;
import com.example.demo.repository.InstructorRepository;
import com.example.demo.service.base.GenericServiceImpl;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class InstructorServiceImpl extends GenericServiceImpl<Instructor, String, InstructorRepository>
        implements InstructorService {

    public InstructorServiceImpl(InstructorRepository instructorRepository) {
        super(instructorRepository);
    }

    @Override
    public boolean login(String username, String password) {
        if (username == null || password == null) return false;
        Optional<Instructor> opt = repository.findById(username);
        boolean success = opt.filter(i -> i.getPassword().equals(password)).isPresent();
        log.info(new StringBuilder().append("Login [").append(username)
                .append("]: ").append(success ? "SUCCESS" : "FAILURE").toString());
        return success;
    }

    @Override
    public Instructor getInstructorByUsername(String username) {
        if (username == null) throw new IllegalArgumentException("Username cannot be null");
        return repository.findById(username).orElse(null);
    }

    @Override
    @Transactional
    public void saveInstructor(Instructor instructor) {
        if (instructor == null) throw new IllegalArgumentException("Instructor cannot be null");
        save(instructor);
        log.info("Saved instructor account.");
    }
}
