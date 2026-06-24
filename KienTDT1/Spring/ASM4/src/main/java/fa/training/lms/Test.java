package fa.training.lms;


import fa.training.lms.model.Course;
import fa.training.lms.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class Test {

    public static void main(String[] args) {
        SpringApplication.run(Test.class, args);
    }

    @Bean
    CommandLineRunner init(CourseService courseService) {
        return args -> {
            courseService.saveCourses(List.of(
                    new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot", 40),
                    new Course("Java Core", "Jane Smith", "Master Java fundamentals", 30)
            ));

            List<Course> saved = courseService.getCourses();
            saved.forEach(System.out::println);
        };
    }
}