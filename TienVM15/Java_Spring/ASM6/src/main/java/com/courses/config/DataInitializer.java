package com.courses.config;

import com.courses.entity.*;
import com.courses.repository.*;
import com.courses.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final InstructorRepository instructorRepository;
    private final LookupRepository lookupRepository;
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final CourseService courseService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Seed Lookups
        if (lookupRepository.count() == 0) {
            lookupRepository.save(Lookup.builder().name("Draft").code(1).type("CourseStatus").position(1).build());
            lookupRepository.save(Lookup.builder().name("Published").code(2).type("CourseStatus").position(2).build());
            lookupRepository.save(Lookup.builder().name("Archived").code(3).type("CourseStatus").position(3).build());

            lookupRepository.save(Lookup.builder().name("Pending").code(1).type("ReviewStatus").position(1).build());
            lookupRepository.save(Lookup.builder().name("Approved").code(2).type("ReviewStatus").position(2).build());
        }

        // 2. Seed Instructor
        if (instructorRepository.count() == 0) {
            Instructor instructor = Instructor.builder()
                    .username("instructor")
                    .password(passwordEncoder.encode("instructor123"))
                    .build();
            instructorRepository.save(instructor);
        }

        // 3. Seed Courses and Reviews
        if (courseRepository.count() == 0) {
            Course c1 = Course.builder()
                    .title("Spring Core & Dependency Injection")
                    .description("Understand the core mechanism of Spring Framework: IoC container, bean scopes, and lifecycle.")
                    .content("# Learning Spring Core\n\nIn this course, you will learn:\n\n* Inversion of Control (IoC)\n* Dependency Injection (DI) with Constructor vs Setter\n* ApplicationContext configurations\n* Custom Bean Lifecycle and Post-Processors.")
                    .status(2) // Published
                    .category("Spring, Java, Core")
                    .build();
            courseRepository.save(c1);

            Course c2 = Course.builder()
                    .title("Thymeleaf & Web MVC Integration")
                    .description("Master dynamic server-side rendering using Spring MVC Controllers and Thymeleaf layouts.")
                    .content("# Master Thymeleaf\n\nLearn how to bind forms, display dynamic lists, and implement layout dialects:\n\n1. Form binding & Spring Validation\n2. Reusable layouts and custom fragments\n3. Dynamic session alerts with RedirectAttributes.")
                    .status(2) // Published
                    .category("Thymeleaf, Web, MVC")
                    .build();
            courseRepository.save(c2);

            Course c3 = Course.builder()
                    .title("Spring Data JPA & Hibernate")
                    .description("Implement modern Object-Relational Mapping (ORM) and manage database persistence with zero-boilerplates.")
                    .content("# JPA & Hibernate\n\nUnderstand relationships, queries, and custom repositories:\n\n* OneToMany and ManyToOne mappings\n* FetchType.LAZY vs EAGER\n* Custom JPA Queries using `@Query`.")
                    .status(2) // Published
                    .category("JPA, Database, Hibernate")
                    .build();
            courseRepository.save(c3);

            Course c4 = Course.builder()
                    .title("Advanced Microservices Architecture")
                    .description("Build scalable distributed systems using Spring Cloud, API Gateway, and Consul.")
                    .content("# Microservices\n\nStudy patterns for enterprise scaling:\n\n* Service discovery & config server\n* Circuit breaker pattern with Resilience4j\n* Distributed tracing.")
                    .status(1) // Draft (Not visible publicly)
                    .category("Microservices, Cloud")
                    .build();
            courseRepository.save(c4);

            // Seed Reviews for published courses
            reviewRepository.save(Review.builder()
                    .authorName("Jane Doe")
                    .email("jane@fpt.com")
                    .rating(5)
                    .content("Absolutely brilliant course! Explained Dependency Injection very clearly.")
                    .status(2) // Approved
                    .course(c1)
                    .build());

            reviewRepository.save(Review.builder()
                    .authorName("Alexander Great")
                    .email("alex@fpt.com")
                    .rating(4)
                    .content("Very good materials. The coding exercises were very helpful.")
                    .status(2) // Approved
                    .course(c1)
                    .build());

            reviewRepository.save(Review.builder()
                    .authorName("John Smith")
                    .email("john.smith@fpt.com")
                    .rating(5)
                    .content("Thymeleaf layouts explained perfectly. Recommended for web developers!")
                    .status(2) // Approved
                    .course(c2)
                    .build());

            reviewRepository.save(Review.builder()
                    .authorName("Sam Wilson")
                    .email("sam@fpt.com")
                    .rating(3)
                    .content("The speed was a bit fast, but content is high quality.")
                    .status(1) // Pending (needs instructor approval)
                    .course(c3)
                    .build());

            // Trigger category frequency calculation
            courseService.updateCategoryFrequencies();
        }
    }
}
