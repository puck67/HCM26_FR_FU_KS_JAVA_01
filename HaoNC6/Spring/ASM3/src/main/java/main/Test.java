package main;

import model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;
import service.CourseService;

import java.util.Arrays;
import java.util.List;

@Component
@ComponentScan(basePackages = {"service", "main"})
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class);
        Test test = context.getBean(Test.class);

        // Create a list of Course objects (e.g., 2-3 courses)
        List<Course> courses = Arrays.asList(
                new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40),
                new Course("Hibernate", "Jane Smith", "Learn ORM with Hibernate.", 30),
                new Course("Java Advanced", "Mike Johnson", "Deep dive into Java concurrency.", 50)
        );

        // Call the saveCourses() method from courseService to save this list to the file.
        test.courseService.saveCourses(courses);

        // Call the getCourses() method from courseService to get the list of saved courses.
        List<Course> savedCourses = test.courseService.getCourses();

        // Loop through the retrieved list and call the displayInfo() method for each course.
        if (savedCourses != null) {
            System.out.println("\n--- Retrieved Courses ---");
            for (Course course : savedCourses) {
                course.displayInfo();
            }
        }
    }
}
