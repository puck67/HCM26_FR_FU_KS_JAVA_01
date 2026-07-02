package com.fpt.lms.service;

import com.fpt.lms.entity.Instructor;
import com.fpt.lms.repository.InstructorRepository;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class InstructorDetailsService implements UserDetailsService {
    private final InstructorRepository instructorRepository;

    public InstructorDetailsService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Instructor instructor = instructorRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return User.withUsername(instructor.getUsername())
            .password(instructor.getPassword())
            .roles("INSTRUCTOR")
            .build();
    }
}

