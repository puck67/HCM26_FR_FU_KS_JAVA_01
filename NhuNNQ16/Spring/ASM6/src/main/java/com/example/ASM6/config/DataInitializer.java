package com.example.ASM6.config;

import com.example.ASM6.model.Course;
import com.example.ASM6.model.Review;
import com.example.ASM6.service.CourseService;
import com.example.ASM6.service.InstructorService;
import com.example.ASM6.service.LookupService;
import com.example.ASM6.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final LookupService lookupService;
    private final InstructorService instructorService;
    private final CourseService courseService;
    private final ReviewService reviewService;

    @Override
    public void run(String... args) throws Exception {
        lookupService.seedDefaultLookups();

        instructorService.seedDefaultInstructor();

        if (courseService.countAllCourses() == 0) {
            
            // Course 1: Java
            Course course1 = Course.builder()
                    .title("Introduction to Java Programming")
                    .description("Learn the fundamentals of Java programming, including OOP concepts, data types, control structures, and collections.")
                    .content("# Introduction to Java\n\nJava is a high-level, class-based, object-oriented programming language designed to have as few implementation dependencies as possible.\n\n## What you will learn:\n1. Variables and Data Types\n2. Object-Oriented Programming (OOP) principles\n3. Exception Handling\n4. Collection Framework\n\n```java\npublic class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, FPT Trainees!\");\n    }\n}\n```")
                    .status(2) // PUBLISHED
                    .category("Java, Programming, Basics")
                    .build();
            courseService.saveCourse(course1);

            // Course 2: Spring Boot
            Course course2 = Course.builder()
                    .title("Spring Boot Masterclass")
                    .description("Learn to build production-grade, enterprise web applications and REST APIs using Spring Boot and Spring Data JPA.")
                    .content("# Spring Boot Masterclass\n\nSpring Boot makes it easy to create stand-alone, production-grade Spring-based Applications that you can \"just run\".\n\n## Topics covered:\n* Dependency Injection & IoC Container\n* Spring MVC & Thymeleaf Templates\n* Database connectivity with JPA/Hibernate\n* Application Security & Custom Interceptors\n\n### Maven Dependency:\n```xml\n<dependency>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>spring-boot-starter-web</artifactId>\n</dependency>\n```")
                    .status(2) // PUBLISHED
                    .category("Java, Spring Boot, Backend, JPA")
                    .build();
            courseService.saveCourse(course2);

            // Course 3: React
            Course course3 = Course.builder()
                    .title("Modern Web Development with React")
                    .description("Build interactive, responsive, and state-of-the-art frontend user interfaces using modern React features like Hooks and Context.")
                    .content("# Modern React Development\n\nReact is a declarative, efficient, and flexible JavaScript library for building user interfaces.\n\n## Core Concepts:\n- Component-Based Architecture\n- JSX syntax\n- Props and State management\n- React Hooks (`useState`, `useEffect`)\n- Integration with REST APIs\n\n### Functional Component Example:\n```javascript\nimport React, { useState } from 'react';\n\nfunction Counter() {\n  const [count, setCount] = useState(0);\n  return <button onClick={() => setCount(count + 1)}>Clicked {count} times</button>;\n}\n```")
                    .status(2) // PUBLISHED
                    .category("React, Frontend, Web Development")
                    .build();
            courseService.saveCourse(course3);

            // Course 4: SQL
            Course course4 = Course.builder()
                    .title("Database Systems & SQL Essentials")
                    .description("A comprehensive guide to relational database design, Normalization, writing SQL queries, joins, subqueries, and indexing.")
                    .content("# Database Systems & SQL\n\nData is the core of every modern application. Learn how to store and query it efficiently using SQL.\n\n## Course outline:\n1. Relational Database Concepts\n2. SQL Select, Insert, Update, Delete statements\n3. Joins (INNER, LEFT, RIGHT, FULL)\n4. Database Normalization (1NF, 2NF, 3NF)\n5. Query Optimization & Indexes")
                    .status(2) // PUBLISHED
                    .category("Database, SQL, Backend")
                    .build();
            courseService.saveCourse(course4);

            // Course 5: Docker & DevOps
            Course course5 = Course.builder()
                    .title("Docker & Kubernetes for Beginners")
                    .description("Understand containerization, write Dockerfiles, compose multi-container architectures, and deploy to Kubernetes clusters.")
                    .content("# DevOps: Containerization and Orchestration\n\nLearn how to package your applications to run reliably in any environment.\n\n## Key concepts:\n- Containers vs Virtual Machines\n- Writing `Dockerfile` and `docker-compose.yml`\n- Building and pushing Docker images\n- Kubernetes Pods, Services, and Deployments\n- CI/CD integration basics")
                    .status(2) // PUBLISHED
                    .category("Docker, DevOps, Cloud, Deployment")
                    .build();
            courseService.saveCourse(course5);

            // Course 6: Microservices (DRAFT)
            Course course6 = Course.builder()
                    .title("Advanced Microservices Architecture")
                    .description("Explore service discovery, API gateways, centralized configuration, and distributed tracing in a Spring Cloud environment.")
                    .content("# Microservices Architecture\n\nThis course is aimed at senior developers looking to scale applications across distributed nodes.\n\n*Draft Content*")
                    .status(1) // DRAFT
                    .category("Spring Boot, Java, Microservices, Cloud")
                    .build();
            courseService.saveCourse(course6);

            // Course 7: Archived Course
            Course course7 = Course.builder()
                    .title("Legacy JSP & Servlet Web Applications")
                    .description("Deep dive into Java Web Technologies from the early 2000s, including JSP, Servlets, and manual JDBC connections.")
                    .content("# JSP and Servlets\n\nThis technology is deprecated in favor of modern frameworks like Spring Boot.\n\n*Archived Content*")
                    .status(3) // ARCHIVED
                    .category("Java, Servlets, Legacy")
                    .build();
            courseService.saveCourse(course7);

            // Add reviews for Course 2 (Spring Boot Masterclass)
            Review r1 = Review.builder()
                    .course(course2)
                    .authorName("Alice Smith")
                    .email("alice@gmail.com")
                    .rating(5)
                    .content("Absolutely amazing course! The instructor goes in-depth on Spring Core concepts. Highly recommended!")
                    .status(2) // APPROVED
                    .build();
            reviewService.createReview(r1);
            reviewService.approveReview(r1.getId()); // approve

            Review r2 = Review.builder()
                    .course(course2)
                    .authorName("Bob Johnson")
                    .email("bob@yahoo.com")
                    .rating(4)
                    .content("Very detailed explanation of JPA and Hibernate. I finally understand cascade types and fetch strategies.")
                    .status(2) // APPROVED
                    .build();
            reviewService.createReview(r2);
            reviewService.approveReview(r2.getId()); // approve

            Review r3 = Review.builder()
                    .course(course2)
                    .authorName("Charlie Brown")
                    .email("charlie@outlook.com")
                    .rating(3)
                    .content("Good pace, but I would love to see more deployment examples using Docker.")
                    .status(1) // PENDING
                    .build();
            reviewService.createReview(r3); // stays pending

            // Add reviews for Course 1 (Java Programming)
            Review r4 = Review.builder()
                    .course(course1)
                    .authorName("David Miller")
                    .email("david@gmail.com")
                    .rating(5)
                    .content("Excellent introduction to OOP. The assignments were challenging but very educational.")
                    .status(2) // APPROVED
                    .build();
            reviewService.createReview(r4);
            reviewService.approveReview(r4.getId()); // approve

            Review r5 = Review.builder()
                    .course(course1)
                    .authorName("Eve Green")
                    .email("eve@gmail.com")
                    .rating(2)
                    .content("Too fast-paced for absolute beginners. Requires prior programming experience.")
                    .status(1) // PENDING
                    .build();
            reviewService.createReview(r5); // stays pending
        }
    }
}
