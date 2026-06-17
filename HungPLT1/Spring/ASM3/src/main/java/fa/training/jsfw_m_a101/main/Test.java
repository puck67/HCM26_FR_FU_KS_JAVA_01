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
        // Initialize Spring Context using Annotation configuration
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        
        // Retrieve the Test bean from the application context to trigger dependency injection
        Test app = context.getBean(Test.class);
        app.run();
        
        // Close context to release resources
        context.close();
    }

    private void run() {
        System.out.println("==================================================");
        System.out.println("          STARTING SPRING CORE COURSE APP         ");
        System.out.println("==================================================");

        // 1. Create a list of Course objects
        List<Course> list = new ArrayList<>();
        list.add(new Course("Spring Framework Core", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        list.add(new Course("Java Core Programming", "Alice Johnson", "Master Java OOP, Collections, and IO Stream.", 60));
        list.add(new Course("Enterprise Web Development", "Robert Davis", "Build enterprise-grade applications.", 45));

        // 2. Call the saveCourses method to save the list to the file
        System.out.println("\n[Action] Saving courses to file...");
        courseService.saveCourses(list);

        // 3. Call the getCourses method to retrieve the saved list
        System.out.println("\n[Action] Loading courses from file...");
        List<Course> loadedCourses = courseService.getCourses();

        // 4. Loop through the retrieved list and display each course info
        System.out.println("\n[Result] Course details:");
        System.out.println("--------------------------------------------------");
        for (Course course : loadedCourses) {
            course.displayInfo();
        }
        System.out.println("--------------------------------------------------");
    }
}
