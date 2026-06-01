package com.example;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Lightweight snapshot test that builds minimal data and prints DB contents.
 * This test is self-contained and can be run independently from the integration suite.
 */
class DatabaseSnapshotTest {
    private static StudentService studentService;
    private static CourseService courseService;

    @BeforeAll
    static void init() {
        assertNotNull(HibernateUtil.getSessionFactory());
        studentService = new StudentServiceImpl();
        courseService = new CourseServiceImpl();
    }

    @AfterAll
    static void shutdown() {
        HibernateUtil.shutdown();
    }

    @Test
    void snapshot() {
        // Seed minimal data (idempotent for a fresh in-memory DB instance)
        Course java = courseService.createCourse("Java Programming", 4);
        Course db = courseService.createCourse("Database Systems", 3);
        Course web = courseService.createCourse("Web Development", 5);
        Student alice = studentService.createStudent("Alice", 22);
        Student bob = studentService.createStudent("Bob", 24);

        studentService.enrollStudentInCourse(alice.getId(), java.getId());
        studentService.enrollStudentInCourse(alice.getId(), db.getId());
        studentService.enrollStudentInCourse(bob.getId(), java.getId());
        studentService.enrollStudentInCourse(bob.getId(), web.getId());

        // Print snapshot
        System.out.println("=== DATABASE SNAPSHOT (standalone test) ===");

        System.out.println("Students:");
        List<Student> students = studentService.getAllStudents();
        students.forEach(student -> System.out.println(" - " + student));

        System.out.println("Courses:");
        List<Course> courses = courseService.getAllCourses();
        courses.forEach(course -> System.out.println(" - " + course));

        System.out.println("Student -> Course rows:");
        List<Object[]> studentCourseRows = studentService.listStudentsWithCourses();
        studentCourseRows.forEach(row -> {
            Student student = (Student) row[0];
            String title = (String) row[1];
            System.out.println(" - " + student.getName() + " -> " + title);
        });

        System.out.println("Course enrollment counts:");
        List<Object[]> courseCounts = courseService.countStudentsInEachCourse();
        courseCounts.forEach(row -> {
            Course course = (Course) row[0];
            Long count = (Long) row[1];
            System.out.println(" - " + course.getTitle() + " | Students enrolled: " + count);
        });
    }
}

