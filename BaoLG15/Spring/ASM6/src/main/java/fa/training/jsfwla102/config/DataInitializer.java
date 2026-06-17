package fa.training.jsfwla102.config;

import fa.training.jsfwla102.entity.Instructor;
import fa.training.jsfwla102.repository.InstructorRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(InstructorRepository instructorRepository, PasswordEncoder passwordEncoder) {
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (instructorRepository.count() == 0) {
            Instructor admin = new Instructor();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            instructorRepository.save(admin);
            System.out.println("Default instructor created: admin / 123456");
        }
    }
}
