package fa.training.hungplt1.asm6.config;

import fa.training.hungplt1.asm6.entity.Instructor;
import fa.training.hungplt1.asm6.repository.InstructorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final InstructorRepository instructorRepository;

    public CustomUserDetailsService(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Instructor instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Instructor not found: " + username));
        
        return new User(
                instructor.getUsername(),
                instructor.getPassword(),
                Collections.emptyList()
        );
    }
}
