package fa.training.jsfw_m_a102.main;

import fa.training.jsfw_m_a102.config.AppConfig;
import fa.training.jsfw_m_a102.model.Course;
import fa.training.jsfw_m_a102.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Test app = context.getBean(Test.class);
        app.run();
        context.close();
    }

    private void run() {
        List<Course> sampleCourses = new ArrayList<>();
        sampleCourses.add(new Course("Spring Framework Core", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        sampleCourses.add(new Course("Java Programming", "Alice Smith", "Master Java OOP and Advanced Concepts.", 60));
        sampleCourses.add(new Course("Web Development", "Bob Wilson", "Build modern full-stack web applications.", 50));

        System.out.println("--- Bắt đầu ghi dữ liệu vào file ---");
        courseService.saveCourses(sampleCourses);
        System.out.println("\n--- Bắt đầu đọc dữ liệu từ file và hiển thị ---");
        List<Course> retrievedCourses = courseService.getCourses();
        for (Course course : retrievedCourses) {
            course.displayInfo();
        }
    }

}
