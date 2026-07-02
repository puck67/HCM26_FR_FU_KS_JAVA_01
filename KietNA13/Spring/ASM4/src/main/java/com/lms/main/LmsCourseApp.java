package com.lms.main;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Application entry point for the Spring LMS demo.
 *
 * <p>This class doubles as the Spring {@link Configuration} root: it is annotated
 * with {@code @ComponentScan} so that all beans under {@code com.lms} are detected
 * and registered automatically.
 *
 * <p>The {@link AnnotationConfigApplicationContext} is opened inside a
 * <em>try-with-resources</em> block so that the Spring context is closed cleanly
 * — and all bean lifecycle callbacks are invoked — even if an exception occurs.
 *
 * <p>This class was formerly named {@code Test}; it has been renamed to
 * {@code LmsCourseApp} to better reflect its role as the application entry point.
 */
@Configuration
@ComponentScan(basePackages = "com.lms")
public class LmsCourseApp {

    private static final Logger log = LoggerFactory.getLogger(LmsCourseApp.class);

    /**
     * Bootstraps the Spring application context, exercises the {@link CourseService}
     * CRUD operations, and then shuts the context down gracefully.
     *
     * @param args command-line arguments (currently unused)
     */
    public static void main(String[] args) {

        // try-with-resources ensures context.close() is called automatically,
        // triggering all @PreDestroy / DisposableBean callbacks.
        try (AnnotationConfigApplicationContext context =
                     new AnnotationConfigApplicationContext(LmsCourseApp.class)) {

            CourseService courseService = context.getBean(CourseService.class);

            // -----------------------------------------------------------------
            // Adding Courses
            // -----------------------------------------------------------------
            log.info("=== Adding Courses ===");

            // Use List.of to create an immutable list of seed courses, then add each one
            List<Course> seedCourses = List.of(
                    new Course("C001", "Java OOP Basics",     30, "John Doe"),
                    new Course("C002", "Spring Framework",    40, "Jane Smith"),
                    new Course("C003", "Hibernate & JPA",     25, "Bob Nguyen"),
                    new Course("C001", "Duplicate Course",    10, "Test") // intentional duplicate to test guard
            );
            // forEach with method reference — replaces explicit for-loop
            seedCourses.forEach(courseService::addNewCourse);

            // -----------------------------------------------------------------
            // Listing All Courses
            // -----------------------------------------------------------------
            log.info("=== All Courses ===");
            courseService.fetchAllCourses().forEach(c -> log.info("{}", c));

            // -----------------------------------------------------------------
            // Finding a Course by ID
            // -----------------------------------------------------------------
            log.info("=== Find Course by ID ===");

            // C001 should be present
            courseService.findCourseById("C001")
                    .ifPresent(c -> log.info("Found: {}", c));

            // C999 does not exist — the orElse branch logs the not-found message
            courseService.findCourseById("C999")
                    .ifPresentOrElse(
                            c  -> log.info("Found: {}", c),
                            () -> log.info("Course C999 not found.")
                    );

            // -----------------------------------------------------------------
            // Updating a Course
            // -----------------------------------------------------------------
            log.info("=== Update Course ===");

            courseService.updateCourseById("C002",
                    new Course("C002", "Spring Boot Advanced", 50, "Jane Smith"));

            courseService.findCourseById("C002")
                    .ifPresent(c -> log.info("After update: {}", c));

            // -----------------------------------------------------------------
            // Deleting a Course
            // -----------------------------------------------------------------
            log.info("=== Delete Course ===");

            courseService.removeCourseById("C003");

            log.info("Courses after deletion:");
            courseService.fetchAllCourses().forEach(c -> log.info("{}", c));

            // -----------------------------------------------------------------
            // Filtering Courses by Minimum Duration
            // -----------------------------------------------------------------
            log.info("=== Courses with Duration >= 40 hours ===");
            courseService.fetchCoursesByMinDuration(40).forEach(c -> log.info("{}", c));

        } // context.close() is called here automatically by try-with-resources
    }
}
