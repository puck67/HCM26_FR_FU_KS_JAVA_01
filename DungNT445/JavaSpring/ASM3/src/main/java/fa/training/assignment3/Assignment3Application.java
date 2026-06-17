package fa.training.assignment3;

import fa.training.assignment3.entity.TrainingCourse;
import fa.training.assignment3.service.TrainingCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Assignment3Application {

    private static final Logger log = LoggerFactory.getLogger(Assignment3Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Assignment3Application.class, args);
    }

    @Bean
    public CommandLineRunner runDemo(TrainingCourseService courseService) {
        return args -> {
            log.info("========== BẮT ĐẦU CHƯƠNG TRÌNH ASM3 ==========");

            // Xóa dữ liệu cũ nếu có
            courseService.removeAll();

            // Tạo và lưu danh sách khóa học mẫu
            courseService.createCourse("Spring Framework Core", "Nguyen Van A", "Nền tảng Spring IoC, DI, AOP", 40);
            courseService.createCourse("Java OOP Nâng cao", "Le Thi B", "Kế thừa, đa hình, trừu tượng", 60);
            courseService.createCourse("Phát triển Web Full-stack", "Tran Van C", "Xây dựng ứng dụng web hiện đại", 50);

            log.info("-> Đã lưu thành công 3 khóa học vào H2 Database.");

            // Đọc và hiển thị dữ liệu từ DB
            log.info("--- Danh sách khóa học trong cơ sở dữ liệu ---");
            List<TrainingCourse> allCourses = courseService.retrieveAll();
            allCourses.forEach(course ->
                    log.info("[ID: {}] {} | Giảng viên: {} | Thời lượng: {} giờ | Mô tả: {}",
                            course.getId(), course.getCourseName(), course.getInstructorFullName(),
                            course.getTotalHours(), course.getSummary())
            );

            log.info("========== KẾT THÚC CHƯƠNG TRÌNH ASM3 ==========");
        };
    }
}
