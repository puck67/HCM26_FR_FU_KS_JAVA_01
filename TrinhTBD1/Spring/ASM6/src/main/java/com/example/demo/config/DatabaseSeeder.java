package com.example.demo.config;

import com.example.demo.model.Course;
import com.example.demo.model.Instructor;
import com.example.demo.model.Lookup;
import com.example.demo.model.Review;
import com.example.demo.service.CourseService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.LookupService;
import com.example.demo.service.ReviewService;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final InstructorService instructorService;
    private final CourseService courseService;
    private final ReviewService reviewService;
    private final LookupService lookupService;

    @Override
    public void run(String... args) throws Exception {
        seedLookups();
        seedInstructor();
        seedCoursesAndReviews();
    }

    private void seedLookups() {
        if (lookupService.getLookupsByType("CourseStatus").isEmpty()) {
            log.info("Seeding CourseStatus lookups...");
            lookupService.saveLookup(new Lookup(null, "CourseStatus", "1", "Draft", 1));
            lookupService.saveLookup(new Lookup(null, "CourseStatus", "2", "Published", 2));
            lookupService.saveLookup(new Lookup(null, "CourseStatus", "3", "Archived", 3));
        }

        if (lookupService.getLookupsByType("ReviewStatus").isEmpty()) {
            log.info("Seeding ReviewStatus lookups...");
            lookupService.saveLookup(new Lookup(null, "ReviewStatus", "1", "Pending", 1));
            lookupService.saveLookup(new Lookup(null, "ReviewStatus", "2", "Approved", 2));
        }
    }

    private void seedInstructor() {
        if (instructorService.getInstructorByUsername("instructor") == null) {
            log.info("Seeding default instructor account (instructor / password)...");
            Instructor instructor = Instructor.builder()
                    .username("instructor")
                    .password("password")
                    .build();
            instructorService.saveInstructor(instructor);
        }
    }

    private void seedCoursesAndReviews() {
        if (courseService.getAllCourses(org.springframework.data.domain.PageRequest.of(0, 1)).getTotalElements() == 0) {
            log.info("Seeding initial courses and reviews...");

            Course htmlCourse = Course.builder()
                    .title("Introduction to HTML")
                    .description("Learn the basics of HTML, elements, structure, and semantic markup.")
                    .content("# Introduction to HTML\n\nThis course covers structural tags, semantic elements, forms, and validation.\n\n## Topics\n- HTML5 Document Structure\n- Common tags (p, div, section, article)\n- Form and Input fields\n- Media tags (img, video)")
                    .status(2)
                    .category("HTML, Web")
                    .createdDate(LocalDateTime.now().minusDays(5))
                    .build();
            courseService.saveCourse(htmlCourse);

            Course cssCourse = Course.builder()
                    .title("Advanced CSS Techniques")
                    .description("Dive deeper into layouts, Flexbox, Grid, animations, and responsive design.")
                    .content("# Advanced CSS Techniques\n\nLearn modern layout systems like CSS Grid and Flexbox, and how to create clean transitions.\n\n## Topics\n- Flexbox alignments\n- Grid areas and responsiveness\n- Keyframe animations\n- Media queries and mobile-first approach")
                    .status(2)
                    .category("CSS, Web, Design")
                    .createdDate(LocalDateTime.now().minusDays(4))
                    .build();
            courseService.saveCourse(cssCourse);

            Course javaCourse = Course.builder()
                    .title("Java Spring Boot 101")
                    .description("Learn the fundamentals of building web applications and REST APIs using Spring Boot.")
                    .content("# Java Spring Boot 101\n\nCreate production-ready web servers using Spring MVC, Spring Data JPA, and Thymeleaf.\n\n## Topics\n- Spring Core (IoC and DI)\n- Spring MVC Controllers and Views\n- Databases with Spring Data JPA\n- Building and testing REST APIs")
                    .status(2)
                    .category("Java, Spring, Backend")
                    .createdDate(LocalDateTime.now().minusDays(3))
                    .build();
            courseService.saveCourse(javaCourse);

            Course sqlCourse = Course.builder()
                    .title("Database Design & SQL")
                    .description("Understand database schemas, tables, relationships, and write efficient SQL queries.")
                    .content("# Database Design & SQL\n\nMaster relational databases, primary/foreign keys, joins, grouping, and indexing.\n\n## Topics\n- Entity-Relationship Diagrams (ERD)\n- Table normalization (1NF, 2NF, 3NF)\n- SQL SELECT, JOIN, GROUP BY\n- Performance tuning and indexing")
                    .status(1)
                    .category("SQL, Database")
                    .createdDate(LocalDateTime.now().minusDays(2))
                    .build();
            courseService.saveCourse(sqlCourse);

            Course reactCourse = Course.builder()
                    .title("React JS for Beginners")
                    .description("Understand component-based architectures, state management, and props in React.")
                    .content("# React JS for Beginners\n\nStep-by-step introduction to functional components, hooks like useState and useEffect.\n\n## Topics\n- JSX syntax\n- Components and Props\n- State Management and Event handling\n- Fetching data using hooks")
                    .status(3)
                    .category("React, Frontend")
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .build();
            courseService.saveCourse(reactCourse);

            reviewService.saveReview(Review.builder()
                    .course(htmlCourse)
                    .authorName("John Doe")
                    .email("john.doe@example.com")
                    .rating(4)
                    .content("This course helped me understand web basics and structure. Highly recommend for absolute beginners!")
                    .status(2)
                    .createdDate(LocalDateTime.now().minusDays(4).plusHours(2))
                    .build());

            reviewService.saveReview(Review.builder()
                    .course(htmlCourse)
                    .authorName("Alice Green")
                    .email("alice.green@example.com")
                    .rating(5)
                    .content("Amazing examples and simple explanations. Loved the quizzes and hands-on exercises!")
                    .status(2)
                    .createdDate(LocalDateTime.now().minusDays(3).plusHours(4))
                    .build());

            reviewService.saveReview(Review.builder()
                    .course(htmlCourse)
                    .authorName("Mark Davis")
                    .email("mark.davis@example.com")
                    .rating(3)
                    .content("Good pace, but could use more advanced HTML5 features in the section.")
                    .status(1)
                    .createdDate(LocalDateTime.now().minusDays(1))
                    .build());

            reviewService.saveReview(Review.builder()
                    .course(cssCourse)
                    .authorName("Emma Watson")
                    .email("emma.watson@example.com")
                    .rating(5)
                    .content("The Flexbox and CSS Grid explanations are the best I have seen so far. Thank you!")
                    .status(2)
                    .createdDate(LocalDateTime.now().minusDays(2).plusHours(1))
                    .build());

            reviewService.saveReview(Review.builder()
                    .course(javaCourse)
                    .authorName("Michael Smith")
                    .email("michael.smith@example.com")
                    .rating(5)
                    .content("Great introduction to Spring Boot and REST API development. Very clear instructions.")
                    .status(2)
                    .createdDate(LocalDateTime.now().minusDays(1).plusHours(6))
                    .build());

            courseService.recalculateCategoryFrequencies();
        }
    }
}
