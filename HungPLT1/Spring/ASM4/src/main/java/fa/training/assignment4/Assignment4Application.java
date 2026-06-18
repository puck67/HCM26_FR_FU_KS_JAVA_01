package fa.training.assignment4;

import fa.training.assignment4.model.Course;
import fa.training.assignment4.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class Assignment4Application implements CommandLineRunner {

    @Autowired
    private CourseService courseService;

    public static void main(String[] args) {
        SpringApplication.run(Assignment4Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        List<Course> courses = new ArrayList<>();
        courses.add(new Course("Spring Framework", "John Doe", "Learn Spring Core, MVC, and Boot.", 40));
        courses.add(new Course("Advanced Java", "Jane Smith", "Deep dive into Java concurrency and streams.", 55));
        courses.add(new Course("Database Design", "Mike Brown", "Relational database modeling and SQL optimization.", 35));

        System.out.println("--- Saving courses to file ---");
        courseService.saveCourses(courses);

        System.out.println("\n--- Loading courses from file ---");
        List<Course> loaded = courseService.getCourses();

        System.out.println("\n--- Course List ---");
        for (Course c : loaded) {
            c.displayInfo();
        }
    }
}
