package com.fsoft.training.courseshare.service;

import com.fsoft.training.courseshare.entity.Instructor;
import com.fsoft.training.courseshare.repository.InstructorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class InstructorDetailsService implements UserDetailsService {

    @Autowired
    private InstructorRepository instructorRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Instructor instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new User(
                instructor.getUsername(),
                instructor.getPassword(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_INSTRUCTOR"))
        );
    }
}
