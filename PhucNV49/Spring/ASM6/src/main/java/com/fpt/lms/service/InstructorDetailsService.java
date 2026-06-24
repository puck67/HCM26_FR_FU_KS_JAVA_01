package com.fpt.lms.service;

import com.fpt.lms.entity.Instructor;
import com.fpt.lms.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InstructorDetailsService implements UserDetailsService {
    @Autowired
    private InstructorRepository instructorRepository;

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
