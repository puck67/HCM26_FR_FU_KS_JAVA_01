package com.example.asm6.security;

import com.example.asm6.model.Instructor;
import com.example.asm6.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Instructor instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Instructor not found: " + username));
        
        return new User(
                instructor.getUsername(),
                instructor.getPassword(),
                Collections.singletonList(() -> "ROLE_INSTRUCTOR")
        );
    }
}
