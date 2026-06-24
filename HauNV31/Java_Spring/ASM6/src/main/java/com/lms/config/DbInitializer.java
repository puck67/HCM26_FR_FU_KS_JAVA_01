package com.lms.config;

import com.lms.model.Course;
import com.lms.model.Instructor;
import com.lms.model.Lookup;
import com.lms.model.Review;
import com.lms.repository.CourseRepository;
import com.lms.repository.InstructorRepository;
import com.lms.repository.LookupRepository;
import com.lms.repository.ReviewRepository;
import com.lms.service.LmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LmsService lmsService;

    @Override
    public void run(String... args) throws Exception {
        if (instructorRepository.count() == 0) {
            instructorRepository.save(new Instructor("instructor", "password"));
        }

        if (lookupRepository.count() == 0) {
            lookupRepository.save(new Lookup("COURSE_STATUS", "1", "DRAFT"));
            lookupRepository.save(new Lookup("COURSE_STATUS", "2", "PUBLISHED"));
            lookupRepository.save(new Lookup("COURSE_STATUS", "3", "ARCHIVED"));
            lookupRepository.save(new Lookup("REVIEW_STATUS", "1", "PENDING"));
            lookupRepository.save(new Lookup("REVIEW_STATUS", "2", "APPROVED"));
        }

        if (courseRepository.count() == 0) {
            Course c1 = new Course();
            c1.setTitle("Spring Framework Basics");
            c1.setDescription("Learn the fundamentals of Spring IoC and DI.");
            c1.setContent("This course covers Spring Core basics including beans, application context, dependency injection, and configuration styles.");
            c1.setStatus(2);
            c1.setCategory("Spring, Java");
            courseRepository.save(c1);

            Course c2 = new Course();
            c2.setTitle("Spring Data JPA Deep Dive");
            c2.setDescription("Master database mapping using JPA and Hibernate in Spring Boot.");
            c2.setContent("In this course, we will look at repositories, entity relationships, custom query methods, and transaction management.");
            c2.setStatus(2);
            c2.setCategory("Spring, JPA, Database");
            courseRepository.save(c2);

            Course c3 = new Course();
            c3.setTitle("Thymeleaf Integration in Spring MVC");
            c3.setDescription("Build dynamic web layouts easily with Thymeleaf.");
            c3.setContent("Learn variables, iteration, condition statements, layout dialect, and integration with Spring validation.");
            c3.setStatus(1);
            c3.setCategory("Thymeleaf, MVC, Web");
            courseRepository.save(c3);

            Review r1 = new Review();
            r1.setAuthorName("Alice");
            r1.setEmail("alice@test.com");
            r1.setRating(5);
            r1.setContent("Excellent introduction to Spring Core! Highly recommend.");
            r1.setStatus(2);
            r1.setCourse(c1);
            reviewRepository.save(r1);

            Review r2 = new Review();
            r2.setAuthorName("Bob");
            r2.setEmail("bob@test.com");
            r2.setRating(4);
            r2.setContent("Good course. Needs more practical hands-on exercises.");
            r2.setStatus(2);
            r2.setCourse(c1);
            reviewRepository.save(r2);

            Review r3 = new Review();
            r3.setAuthorName("Charlie");
            r3.setEmail("charlie@test.com");
            r3.setRating(5);
            r3.setContent("Loved the JPA mapping details. Very clear.");
            r3.setStatus(1);
            r3.setCourse(c2);
            reviewRepository.save(r3);

            lmsService.syncCategories();
        }
    }
}
