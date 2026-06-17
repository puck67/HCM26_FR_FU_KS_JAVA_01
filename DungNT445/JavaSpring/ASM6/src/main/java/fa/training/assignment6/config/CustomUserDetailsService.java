package fa.training.assignment6.config;

import fa.training.assignment6.entity.Trainer;
import fa.training.assignment6.repository.TrainerRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final TrainerRepository instructorRepository;

    public CustomUserDetailsService(TrainerRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Trainer instructor = instructorRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Không tìm thấy instructor với username: " + username));
        
        return new User(
                instructor.getUsername(),
                instructor.getPassword(),
                Collections.emptyList()
        );
    }
}
