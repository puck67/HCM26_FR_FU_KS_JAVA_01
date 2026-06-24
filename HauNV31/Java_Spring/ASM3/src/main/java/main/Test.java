package main;

import config.AppConfig;
import model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;
import service.CourseService;

import java.util.Arrays;
import java.util.List;

@Component
public class Test {

    @Autowired
    CourseService courseService;

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class);
        Test test = context.getBean(Test.class);

        List<Course> courses = Arrays.asList(
            new Course("Spring Core", "John Doe", "Introduction to Spring Core", 40),
            new Course("Spring MVC", "Jane Smith", "Building web apps with Spring MVC", 30)
        );

        test.courseService.saveCourses(courses);

        List<Course> loaded = test.courseService.getCourses();
        loaded.forEach(Course::displayInfo);
    }
}
