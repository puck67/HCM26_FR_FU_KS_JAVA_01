package com.lms.config;

import com.lms.model.*;
import com.lms.repository.*;
import com.lms.service.LmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private LmsService lmsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Initialize Lookups
        if (lookupRepository.count() == 0) {
            lookupRepository.saveAll(Arrays.asList(
                    new Lookup("course_status", 1, "Draft"),
                    new Lookup("course_status", 2, "Published"),
                    new Lookup("course_status", 3, "Archived"),
                    new Lookup("review_status", 1, "Pending"),
                    new Lookup("review_status", 2, "Approved")
            ));
        }

        // Initialize Instructor
        if (instructorRepository.count() == 0) {
            String encodedPassword = passwordEncoder.encode("password");
            instructorRepository.save(new Instructor("instructor", encodedPassword));
        }

        // Initialize Sample Courses
        if (courseRepository.count() == 0) {
            Course c1 = new Course(
                    "Spring Framework Core",
                    "Learn the core concepts of Spring including IoC, DI, and bean lifecycles.",
                    "# Spring Core Training\n\nWelcome to Spring Core! In this course, you will learn:\n\n* **Inversion of Control (IoC)**\n* **Dependency Injection (DI)**\n* Spring Bean lifecycles\n* ApplicationContext configurations\n\nEnjoy the course!",
                    2, // PUBLISHED
                    "Spring, Java, Core"
            );

            Course c2 = new Course(
                    "Mastering Spring Boot",
                    "Take your Java web development to the next level with Spring Boot.",
                    "# Mastering Spring Boot\n\nDeep dive into auto-configuration, custom starters, Spring Security, and database integrations.\n\n### Prerequisites\n* Java Core\n* Spring Core Concepts\n\nLet's get started!",
                    2, // PUBLISHED
                    "Spring Boot, Web, Security"
            );

            Course c3 = new Course(
                    "Java Web Development with Spring MVC",
                    "Build dynamic, secure enterprise web applications using Spring MVC and Thymeleaf.",
                    "# Java Web Development\n\nLearn Spring MVC pattern, RESTful endpoints, data validation, and Thymeleaf templating.\n\n### Features\n- Interactive web interfaces\n- Robust server logic\n- Spring Data JPA connection",
                    2, // PUBLISHED
                    "Java, Spring MVC, Thymeleaf"
            );

            Course c4 = new Course(
                    "Intro to Microservices with Spring Cloud",
                    "Design and build scalable microservice architectures.",
                    "# Intro to Microservices\n\nLearn Service Discovery (Eureka), API Gateway, Config Server, and circuit breakers.",
                    1, // DRAFT
                    "Microservices, Spring Cloud"
            );

            courseRepository.saveAll(Arrays.asList(c1, c2, c3, c4));

            // Initialize Sample Reviews
            reviewRepository.saveAll(Arrays.asList(
                    new Review(c1, "Jane Smith", "jane@fpt.com", 5, "This course is amazing! The explanation of IoC and DI was very clear and easy to understand.", 2), // APPROVED
                    new Review(c1, "Mike Johnson", "mike@fpt.com", 4, "Great introduction to Spring. Highly recommended for beginners.", 2), // APPROVED
                    new Review(c2, "Alice Williams", "alice@fpt.com", 5, "Spring Boot is so powerful. This course helped me understand the magic under the hood.", 2), // APPROVED
                    new Review(c2, "John Doe", "john@fpt.com", 3, "Decent course, but would like to see more advanced topics.", 1), // PENDING
                    new Review(c3, "David Brown", "david@fpt.com", 5, "Excellent guide! Build real applications and deploy them.", 2) // APPROVED
            ));

            // Recalculate categories
            lmsService.recalculateCategoryFrequencies();
        }
    }
}
