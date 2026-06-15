package main;

import model.Course;
import service.CourseService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Configuration
@ComponentScan(basePackages = {"service", "main"})
public class Test {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test.class);
        Test testBean = context.getBean(Test.class);
        
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        courses.add(new Course("Java Programming", "Alice", "Learn object-oriented programming concepts and IO operations.", 35));
        courses.add(new Course("Web Development", "Bob", "Build modern web applications with HTML, CSS, and JS.", 50));

        testBean.courseService.saveCourses(courses);

        System.out.println("--- Retrieved Courses ---");
        List<Course> retrievedCourses = testBean.courseService.getCourses();
        if (retrievedCourses != null) {
            for (Course course : retrievedCourses) {
                course.displayInfo();
            }
        }
    }
}
