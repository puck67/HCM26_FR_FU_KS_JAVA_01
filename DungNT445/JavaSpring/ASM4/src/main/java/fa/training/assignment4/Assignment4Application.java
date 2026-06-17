package fa.training.assignment4;

import fa.training.assignment4.entity.LmsCourse;
import fa.training.assignment4.service.LmsCourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Assignment4Application {

    private static final Logger log = LoggerFactory.getLogger(Assignment4Application.class);

    public static void main(String[] args) {
        SpringApplication.run(Assignment4Application.class, args);
    }

    @Bean
    public CommandLineRunner runDemo(LmsCourseService courseService) {
        return args -> {
            log.info("========== BẮT ĐẦU CHƯƠNG TRÌNH ==========");
            
            // Xóa dữ liệu cũ nếu có
            courseService.deleteAllCourses();
            
            // Tạo danh sách các khóa học mẫu
            List<LmsCourse> initialCourses = Arrays.asList(
                    new LmsCourse("Spring Boot MVC", "Nguyen Van A", "Hoc Spring Boot nang cao", 45),
                    new LmsCourse("Java Core & OOP", "Le Thi B", "Kien thuc nen tang ve Java", 60),
                    new LmsCourse("Frontend voi ReactJS", "Tran Van C", "Xay dung giao dien hien dai", 50)
            );

            log.info("--- 1. Bắt đầu lưu dữ liệu vào H2 Database ---");
            courseService.saveAll(initialCourses);
            log.info("-> Lưu thành công {} khóa học.", initialCourses.size());

            log.info("--- 2. Lấy dữ liệu từ H2 Database và hiển thị ---");
            List<LmsCourse> dbCourses = courseService.findAllCourses();
            for (LmsCourse c : dbCourses) {
                log.info("[ID: {}] {} | Giảng viên: {} | Thời lượng: {} giờ | Mô tả: {}",
                        c.getId(), c.getCourseTitle(), c.getTrainerName(), c.getHours(), c.getCourseDescription());
            }
            
            log.info("========== KẾT THÚC CHƯƠNG TRÌNH ==========");
        };
    }
}
