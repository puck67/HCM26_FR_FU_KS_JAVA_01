package com.lms.security;

import com.lms.model.Instructor;
import com.lms.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Instructor instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Instructor not found: " + username));

        return User.withUsername(instructor.getUsername())
                .password(instructor.getPassword())
                .roles("INSTRUCTOR")
                .build();
    }
}
