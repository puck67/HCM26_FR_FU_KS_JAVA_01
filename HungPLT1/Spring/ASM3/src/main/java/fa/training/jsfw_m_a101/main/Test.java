package fa.training.jsfw_m_a101.main;

import fa.training.jsfw_m_a101.config.AppConfig;
import fa.training.jsfw_m_a101.model.Course;
import fa.training.jsfw_m_a101.service.CourseService;
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
        System.out.println("==================================================");
        System.out.println("          STARTING SPRING CORE COURSE APP         ");
        System.out.println("==================================================");

        List<Course> list = new ArrayList<>();
        list.add(new Course("Spring Framework Core", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        list.add(new Course("Java Core Programming", "Alice Johnson", "Master Java OOP, Collections, and IO Stream.", 60));
        list.add(new Course("Enterprise Web Development", "Robert Davis", "Build enterprise-grade applications.", 45));

        System.out.println("\n[Action] Saving courses to file...");
        courseService.saveCourses(list);

        System.out.println("\n[Action] Loading courses from file...");
        List<Course> loadedCourses = courseService.getCourses();

        System.out.println("\n[Result] Course details:");
        System.out.println("--------------------------------------------------");
        for (Course course : loadedCourses) {
            course.displayInfo();
        }
        System.out.println("--------------------------------------------------");
    }
}
