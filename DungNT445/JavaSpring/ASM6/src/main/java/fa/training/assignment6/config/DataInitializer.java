package fa.training.assignment6.config;

import fa.training.assignment6.entity.Trainer;
import fa.training.assignment6.repository.TrainerRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final TrainerRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(TrainerRepository instructorRepository, PasswordEncoder passwordEncoder) {
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (instructorRepository.count() == 0) {
            Trainer admin = new Trainer();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("123456"));
            instructorRepository.save(admin);
            System.out.println("Default instructor created: admin / 123456");
        }
    }
}
