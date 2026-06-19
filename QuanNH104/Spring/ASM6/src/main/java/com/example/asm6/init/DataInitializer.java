package com.example.asm6.init;

import com.example.asm6.model.Course;
import com.example.asm6.model.Instructor;
import com.example.asm6.model.Lookup;
import com.example.asm6.model.Review;
import com.example.asm6.repository.CourseRepository;
import com.example.asm6.repository.InstructorRepository;
import com.example.asm6.repository.LookupRepository;
import com.example.asm6.repository.ReviewRepository;
import com.example.asm6.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private LookupRepository lookupRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // 1. Khởi tạo Instructor
        if (instructorRepository.count() == 0) {
            Instructor instructor = Instructor.builder()
                    .username("instructor")
                    .password(passwordEncoder.encode("123456"))
                    .fullName("John Doe")
                    .build();
            instructorRepository.save(instructor);
        }

        // 2. Khởi tạo Lookup
        if (lookupRepository.count() == 0) {
            lookupRepository.save(Lookup.builder().type("COURSE_STATUS").code("1").value("Draft").build());
            lookupRepository.save(Lookup.builder().type("COURSE_STATUS").code("2").value("Published").build());
            lookupRepository.save(Lookup.builder().type("COURSE_STATUS").code("3").value("Archived").build());
            
            lookupRepository.save(Lookup.builder().type("REVIEW_STATUS").code("1").value("Pending").build());
            lookupRepository.save(Lookup.builder().type("REVIEW_STATUS").code("2").value("Approved").build());
        }

        // 3. Khởi tạo Courses và Reviews mẫu
        if (courseRepository.count() == 0) {
            // Khóa học 1
            Course c1 = Course.builder()
                    .title("Introduction to HTML")
                    .description("Learn the basics of HTML, elements, structure, and semantic markup.")
                    .content("# Introduction to HTML\n\nThis course covers the fundamentals of HTML5.\n\n## What you will learn:\n- HTML Tags\n- Document Structure\n- Forms and Inputs\n- Semantic Elements\n\nGet ready to build your first web page!")
                    .status(2) // Published
                    .category("HTML, CSS")
                    .createdAt(LocalDateTime.now().minusDays(5))
                    .build();
            c1 = courseRepository.save(c1);

            reviewRepository.save(Review.builder()
                    .authorName("John Doe")
                    .email("john@example.com")
                    .rating(5)
                    .content("This course helped me understand web basics and structure. Great teacher!")
                    .status(2) // Approved
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .course(c1)
                    .build());

            reviewRepository.save(Review.builder()
                    .authorName("Alice Green")
                    .email("alice@example.com")
                    .rating(4)
                    .content("Amazing examples, detailed explanations, highly recommended.")
                    .status(2) // Approved
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .course(c1)
                    .build());

            // Khóa học 2
            Course c2 = Course.builder()
                    .title("Advanced CSS Techniques")
                    .description("Dive deeper into CSS Grid, Flexbox, transitions, and modern responsive design.")
                    .content("# Advanced CSS Techniques\n\nMaster the art of layouts and animations using modern CSS.\n\n## Course Outline:\n- Flexbox deep dive\n- CSS Grid Layout\n- Transitions and Keyframe Animations\n- CSS Custom Properties (Variables)\n\nMake your websites look stunning!")
                    .status(2) // Published
                    .category("CSS, HTML")
                    .createdAt(LocalDateTime.now().minusDays(4))
                    .build();
            c2 = courseRepository.save(c2);

            reviewRepository.save(Review.builder()
                    .authorName("Michael Smith")
                    .email("michael@example.com")
                    .rating(5)
                    .content("Loved the Flexbox and Grid tutorials! Extremely useful.")
                    .status(2) // Approved
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .course(c2)
                    .build());

            // Khóa học 3
            Course c3 = Course.builder()
                    .title("Java Spring Boot 101")
                    .description("Great introduction to Spring Boot and REST API development.")
                    .content("# Java Spring Boot 101\n\nBuild robust backend applications with Java and Spring Boot framework.\n\n## What is inside:\n- Dependency Injection & IoC\n- Spring MVC Controllers\n- Spring Data JPA with H2 Database\n- Building RESTful APIs\n\nStart your backend developer journey today!")
                    .status(2) // Published
                    .category("Java, Spring")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build();
            c3 = courseRepository.save(c3);

            reviewRepository.save(Review.builder()
                    .authorName("Bob Johnson")
                    .email("bob@example.com")
                    .rating(4)
                    .content("Very solid introduction. Helped me get started with Spring Boot fast.")
                    .status(2) // Approved
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .course(c3)
                    .build());

            // Khóa học 4 (Draft)
            Course c4 = Course.builder()
                    .title("MySQL Database Essentials")
                    .description("Master relational databases, SQL queries, joins, and indexing.")
                    .content("# MySQL Database Essentials\n\nEverything you need to know about designing database tables and querying data.\n\n- SQL Basics\n- Table Joins (Inner, Left, Right)\n- Indexes and Performance Tuning")
                    .status(1) // Draft
                    .category("MySQL, SQL")
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build();
            courseRepository.save(c4);

            // Khóa học 5 (Published)
            Course c5 = Course.builder()
                    .title("Advanced HTML5 Features")
                    .description("Dive deeper into HTML5 multimedia tags, APIs, and best practices.")
                    .content("# Advanced HTML5 Features\n\nExplore video, audio, canvas, and geolocation APIs in modern browsers.\n\n## Content:\n- Audio & Video tags\n- HTML5 Canvas drawings\n- Web Storage (localStorage, sessionStorage)")
                    .status(2) // Published
                    .category("HTML")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build();
            courseRepository.save(c5);
            
            // Tính toán lại tần suất danh mục ngay sau khi tạo
            categoryService.recalculateCategoryFrequencies();
        }
    }
}
