package fa.training.assignment5;

import fa.training.assignment5.entity.Learner;
import fa.training.assignment5.entity.SystemInstructor;
import fa.training.assignment5.entity.SystemUser;
import fa.training.assignment5.service.LmsDatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Assignment5Application {

    private static final Logger log = LoggerFactory.getLogger(Assignment5Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Assignment5Application.class, args);
    }

    @Bean
    public CommandLineRunner initDatabase(LmsDatabaseService dataService) {
        return args -> {
            log.info("========== BẮT ĐẦU CHƯƠNG TRÌNH ASM5 ==========");
            
            // Khởi tạo danh sách user (Đa hình: SystemUser và SystemInstructor)
            List<SystemUser> users = Arrays.asList(
                    new SystemUser("Normal User A", "user.a@fpt.com"),
                    new SystemInstructor("Instructor B", "inst.b@fpt.com", "IT", "Chuyen gia Spring Boot")
            );
            dataService.saveSystemUsers(users);

            // Khởi tạo danh sách sinh viên
            List<Learner> learners = Arrays.asList(
                    new Learner("Learner X", 8.5),
                    new Learner("Learner Y", 7.2)
            );
            dataService.saveLearners(learners);

            log.info("--- Hiển thị đa hình (Polymorphism) từ DB ---");
            List<SystemUser> savedUsers = dataService.getAllSystemUsers();
            for (SystemUser u : savedUsers) {
                u.displayDetails();
            }

            log.info("--- Hiển thị danh sách Learner từ DB ---");
            List<Learner> savedLearners = dataService.getAllLearners();
            for (Learner l : savedLearners) {
                l.showInfo();
            }
            
            log.info("========== KẾT THÚC CHƯƠNG TRÌNH ASM5 ==========");
        };
    }
}
