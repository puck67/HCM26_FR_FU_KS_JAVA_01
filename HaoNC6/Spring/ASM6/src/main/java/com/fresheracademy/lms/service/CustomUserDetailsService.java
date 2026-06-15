package com.fresheracademy.lms.service;

import com.fresheracademy.lms.entity.Instructor;
import com.fresheracademy.lms.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Fallback or handle via DB
        if ("instructor".equals(username)) {
            return User.withUsername("instructor")
                    .password("{noop}password")
                    .roles("INSTRUCTOR")
                    .build();
        }
        
        Optional<Instructor> instructor = instructorRepository.findByUsername(username);
        if (instructor.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }
        
        return User.withUsername(instructor.get().getUsername())
                .password(instructor.get().getPassword())
                .roles("INSTRUCTOR")
                .build();
    }
}
