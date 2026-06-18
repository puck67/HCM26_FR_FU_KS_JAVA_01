package fa.training.JSFW_M_A101;

import fa.training.JSFW_M_A101.model.Course;
import fa.training.JSFW_M_A101.service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = "fa.training.JSFW_M_A101")
public class Test {

    // Attribute: CourseService (injected from Spring container)
    private CourseService courseService;

    public static void main(String[] args) {
        // Create Spring Application Context using annotation-based configuration
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class);

        // Get the Test bean and CourseService bean from the container
        CourseService courseService = context.getBean(CourseService.class);

        // Step 1: Create a list of Course objects (3 courses)
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Framework", "John Doe",
                "Learn Spring Core, MVC, and Boot", 40));
        courses.add(new Course("Java Programming", "Jane Smith",
                "Master Java SE fundamentals and OOP", 60));
        courses.add(new Course("Database Design", "Bob Johnson",
                "SQL, normalization, and ER modeling", 30));

        // Step 2: Save the list of courses to file using CourseService
        System.out.println("=== Saving Courses ===");
        courseService.saveCourses(courses);

        // Step 3: Read the courses back from file
        System.out.println("\n=== Loading Courses ===");
        List<Course> loadedCourses = courseService.getCourses();

        // Step 4: Loop through and display info for each course
        System.out.println("\n=== Displaying Course Info ===");
        for (Course course : loadedCourses) {
            course.displayInfo();
        }

        // Close the context
        ((AnnotationConfigApplicationContext) context).close();
    }
}
