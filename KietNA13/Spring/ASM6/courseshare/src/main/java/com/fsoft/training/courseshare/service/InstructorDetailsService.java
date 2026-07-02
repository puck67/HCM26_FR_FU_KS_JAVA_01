package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Instructor;
import com.fsoft.training.courseshare.repository.InstructorRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class InstructorDetailsService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(InstructorDetailsService.class);

    private final InstructorRepository instructorRepository;

    public InstructorDetailsService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("Attempting to load instructor by username: {}", username);
        Instructor instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Instructor not found: {}", username);
                    return new UsernameNotFoundException("User not found: " + username);
                });

        return new User(
                instructor.getUsername(),
                instructor.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
        );
    }
}
