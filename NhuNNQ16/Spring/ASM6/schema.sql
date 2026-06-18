-- Drop tables if they exist to start fresh (in correct order of dependencies)
DROP TABLE IF EXISTS tbl_review CASCADE;
DROP TABLE IF EXISTS tbl_course CASCADE;
DROP TABLE IF EXISTS tbl_instructor CASCADE;
DROP TABLE IF EXISTS tbl_category CASCADE;
DROP TABLE IF EXISTS tbl_lookup CASCADE;

-- 1. Create tbl_lookup
CREATE TABLE tbl_lookup (
    id SERIAL PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    code VARCHAR(255) NOT NULL,
    lookup_value VARCHAR(255) NOT NULL
);

-- 2. Create tbl_instructor
CREATE TABLE tbl_instructor (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL
);

-- 3. Create tbl_course
CREATE TABLE tbl_course (
    id SERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    description VARCHAR(1000) NOT NULL,
    content TEXT NOT NULL,
    status INTEGER NOT NULL,
    category VARCHAR(255),
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 4. Create tbl_review
CREATE TABLE tbl_review (
    id SERIAL PRIMARY KEY,
    course_id BIGINT NOT NULL REFERENCES tbl_course(id) ON DELETE CASCADE,
    author_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    rating INTEGER NOT NULL,
    content VARCHAR(2000) NOT NULL,
    status INTEGER NOT NULL DEFAULT 1,
    created_at TIMESTAMP WITHOUT TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 5. Create tbl_category
CREATE TABLE tbl_category (
    name VARCHAR(255) PRIMARY KEY,
    frequency INTEGER NOT NULL DEFAULT 0
);

-- =========================================================================
-- SEED DATA (INITIAL DATA)
-- =========================================================================

-- Seed default lookups
INSERT INTO tbl_lookup (type, code, lookup_value) VALUES 
('COURSE_STATUS', '1', 'Draft'),
('COURSE_STATUS', '2', 'Published'),
('COURSE_STATUS', '3', 'Archived'),
('REVIEW_STATUS', '1', 'Pending'),
('REVIEW_STATUS', '2', 'Approved');

-- Seed default instructor (username: admin, password: admin)
INSERT INTO tbl_instructor (username, password) VALUES 
('admin', 'admin');

-- Seed default courses
INSERT INTO tbl_course (title, description, content, status, category, created_at) VALUES 
('Introduction to Java Programming', 
 'Learn the fundamentals of Java programming, including OOP concepts, data types, control structures, and collections.', 
 '# Introduction to Java\n\nJava is a high-level, class-based, object-oriented programming language designed to have as few implementation dependencies as possible.\n\n## What you will learn:\n1. Variables and Data Types\n2. Object-Oriented Programming (OOP) principles\n3. Exception Handling\n4. Collection Framework\n\n```java\npublic class HelloWorld {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, FPT Trainees!\");\n    }\n}\n```', 
 2, 'Java, Programming, Basics', CURRENT_TIMESTAMP - INTERVAL '5 days'),

('Spring Boot Masterclass', 
 'Learn to build production-grade, enterprise web applications and REST APIs using Spring Boot and Spring Data JPA.', 
 '# Spring Boot Masterclass\n\nSpring Boot makes it easy to create stand-alone, production-grade Spring-based Applications that you can \"just run\".\n\n## Topics covered:\n* Dependency Injection & IoC Container\n* Spring MVC & Thymeleaf Templates\n* Database connectivity with JPA/Hibernate\n* Application Security & Custom Interceptors\n\n### Maven Dependency:\n```xml\n<dependency>\n    <groupId>org.springframework.boot</groupId>\n    <artifactId>spring-boot-starter-web</artifactId>\n</dependency>\n```', 
 2, 'Java, Spring Boot, Backend, JPA', CURRENT_TIMESTAMP - INTERVAL '4 days'),

('Modern Web Development with React', 
 'Build interactive, responsive, and state-of-the-art frontend user interfaces using modern React features like Hooks and Context.', 
 '# Modern React Development\n\nReact is a declarative, efficient, and flexible JavaScript library for building user interfaces.\n\n## Core Concepts:\n- Component-Based Architecture\n- JSX syntax\n- Props and State management\n- React Hooks (`useState`, `useEffect`)\n- Integration with REST APIs\n\n### Functional Component Example:\n```javascript\nimport React, { useState } from ''react'';\n\nfunction Counter() {\n  const [count, setCount] = useState(0);\n  return <button onClick={() => setCount(count + 1)}>Clicked {count} times</button>;\n}\n```', 
 2, 'React, Frontend, Web Development', CURRENT_TIMESTAMP - INTERVAL '3 days'),

('Database Systems & SQL Essentials', 
 'A comprehensive guide to relational database design, Normalization, writing SQL queries, joins, subqueries, and indexing.', 
 '# Database Systems & SQL\n\nData is the core of every modern application. Learn how to store and query it efficiently using SQL.\n\n## Course outline:\n1. Relational Database Concepts\n2. SQL Select, Insert, Update, Delete statements\n3. Joins (INNER, LEFT, RIGHT, FULL)\n4. Database Normalization (1NF, 2NF, 3NF)\n5. Query Optimization & Indexes', 
 2, 'Database, SQL, Backend', CURRENT_TIMESTAMP - INTERVAL '2 days'),

('Docker & Kubernetes for Beginners', 
 'Understand containerization, write Dockerfiles, compose multi-container architectures, and deploy to Kubernetes clusters.', 
 '# DevOps: Containerization and Orchestration\n\nLearn how to package your applications to run reliably in any environment.\n\n## Key concepts:\n- Containers vs Virtual Machines\n- Writing `Dockerfile` and `docker-compose.yml`\n- Building and pushing Docker images\n- Kubernetes Pods, Services, and Deployments\n- CI/CD integration basics', 
 2, 'Docker, DevOps, Cloud, Deployment', CURRENT_TIMESTAMP - INTERVAL '1 day'),

('Advanced Microservices Architecture', 
 'Explore service discovery, API gateways, centralized configuration, and distributed tracing in a Spring Cloud environment.', 
 '# Microservices Architecture\n\nThis course is aimed at senior developers looking to scale applications across distributed nodes.\n\n*Draft Content*', 
 1, 'Spring Boot, Java, Microservices, Cloud', CURRENT_TIMESTAMP),

('Legacy JSP & Servlet Web Applications', 
 'Deep dive into Java Web Technologies from the early 2000s, including JSP, Servlets, and manual JDBC connections.', 
 '# JSP and Servlets\n\nThis technology is deprecated in favor of modern frameworks like Spring Boot.\n\n*Archived Content*', 
 3, 'Java, Servlets, Legacy', CURRENT_TIMESTAMP - INTERVAL '10 days');

-- Seed default reviews
-- (Assuming IDs generated are 1 to 7 for courses in sequence of insert)
INSERT INTO tbl_review (course_id, author_name, email, rating, content, status, created_at) VALUES 
(2, 'Alice Smith', 'alice@gmail.com', 5, 'Absolutely amazing course! The instructor goes in-depth on Spring Core concepts. Highly recommended!', 2, CURRENT_TIMESTAMP - INTERVAL '2 hours'),
(2, 'Bob Johnson', 'bob@yahoo.com', 4, 'Very detailed explanation of JPA and Hibernate. I finally understand cascade types and fetch strategies.', 2, CURRENT_TIMESTAMP - INTERVAL '1 hour'),
(2, 'Charlie Brown', 'charlie@outlook.com', 3, 'Good pace, but I would love to see more deployment examples using Docker.', 1, CURRENT_TIMESTAMP),
(1, 'David Miller', 'david@gmail.com', 5, 'Excellent introduction to OOP. The assignments were challenging but very educational.', 2, CURRENT_TIMESTAMP - INTERVAL '5 hours'),
(1, 'Eve Green', 'eve@gmail.com', 2, 'Too fast-paced for absolute beginners. Requires prior programming experience.', 1, CURRENT_TIMESTAMP);
