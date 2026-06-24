package fa.training.hungplt1.asm6.config;

import fa.training.hungplt1.asm6.entity.Instructor;
import fa.training.hungplt1.asm6.repository.InstructorRepository;
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
        }
    }
}
