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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ServiceIntegrationTest {
    private static StudentService studentService;
    private static CourseService courseService;
    private static Integer aliceId;
    private static Integer bobId;
    private static Integer javaId;
    private static Integer dbId;
    private static Integer webId;

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
    @Order(1)
    void createAndEnrollData() {
        Course java = courseService.createCourse("Java Programming", 4);
        Course db = courseService.createCourse("Database Systems", 3);
        Course web = courseService.createCourse("Web Development", 5);
        Student alice = studentService.createStudent("Alice", 22);
        Student bob = studentService.createStudent("Bob", 24);

        javaId = java.getId();
        dbId = db.getId();
        webId = web.getId();
        aliceId = alice.getId();
        bobId = bob.getId();

        assertNotNull(javaId);
        assertNotNull(dbId);
        assertNotNull(webId);
        assertNotNull(aliceId);
        assertNotNull(bobId);

        assertTrue(studentService.enrollStudentInCourse(aliceId, javaId));
        assertTrue(studentService.enrollStudentInCourse(aliceId, dbId));
        assertTrue(studentService.enrollStudentInCourse(bobId, javaId));
        assertTrue(studentService.enrollStudentInCourse(bobId, webId));

        assertEquals(2, studentService.getCoursesOfStudent(aliceId).size());
        assertEquals(2, courseService.getStudentsOfCourse(javaId).size());
    }

    @Test
    @Order(2)
    void advancedQueriesAndUpdatesWork() {
        Student updatedAlice = studentService.updateStudent(aliceId, "Alice Smith", 23);
        assertNotNull(updatedAlice);
        assertEquals("Alice Smith", updatedAlice.getName());
        assertEquals(23, updatedAlice.getAge());

        List<Student> olderThanTwentyOne = studentService.findStudentsOlderThan(21);
        assertTrue(olderThanTwentyOne.stream().anyMatch(s -> s.getName().equals("Alice Smith")));
        assertTrue(olderThanTwentyOne.stream().anyMatch(s -> s.getName().equals("Bob")));

        List<Student> byName = studentService.findStudentsByName("Ali");
        assertEquals(1, byName.size());
        assertEquals("Alice Smith", byName.get(0).getName());
        assertEquals(2, byName.get(0).getCourses().size());

        List<Object[]> joined = studentService.listStudentsWithCourses();
        assertTrue(joined.stream().anyMatch(row -> ((Student) row[0]).getName().equals("Alice Smith") && "Java Programming".equals(row[1])));

        List<Course> creditGreaterThanThree = courseService.findCoursesWithCreditGreaterThan(3);
        assertEquals(2, creditGreaterThanThree.size());
        assertTrue(creditGreaterThanThree.stream().anyMatch(c -> c.getTitle().equals("Java Programming")));
        assertTrue(creditGreaterThanThree.stream().anyMatch(c -> c.getTitle().equals("Web Development")));

        List<Object[]> counts = courseService.countStudentsInEachCourse();
        assertEquals(3, counts.size());
        assertTrue(counts.stream().anyMatch(row -> ((Course) row[0]).getTitle().equals("Java Programming") && ((Long) row[1]) == 2L));
        assertTrue(counts.stream().anyMatch(row -> ((Course) row[0]).getTitle().equals("Database Systems") && ((Long) row[1]) == 1L));
        assertTrue(counts.stream().anyMatch(row -> ((Course) row[0]).getTitle().equals("Web Development") && ((Long) row[1]) == 1L));

        List<Student> studentsInJava = studentService.findStudentsEnrolledInCourse(javaId);
        assertEquals(2, studentsInJava.size());
        assertTrue(studentsInJava.stream().anyMatch(s -> s.getName().equals("Alice Smith")));
        assertTrue(studentsInJava.stream().anyMatch(s -> s.getName().equals("Bob")));

        assertTrue(studentService.removeStudentFromCourse(aliceId, dbId));
        assertEquals(1, studentService.getCoursesOfStudent(aliceId).size());
    }

    @Test
    @Order(3)
    void inspectDatabaseSnapshot() {
        System.out.println("=== DATABASE SNAPSHOT ===");

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

        assertEquals(2, students.size());
        assertEquals(3, courses.size());
        assertEquals(1, studentService.getCoursesOfStudent(aliceId).size());
        assertEquals(2, studentService.getCoursesOfStudent(bobId).size());
        assertEquals(2, courseService.getStudentsOfCourse(javaId).size());
        assertEquals(0, courseService.getStudentsOfCourse(dbId).size());
        assertEquals(1, courseService.getStudentsOfCourse(webId).size());
        assertTrue(studentCourseRows.stream().anyMatch(row -> ((Student) row[0]).getName().equals("Alice Smith") && "Java Programming".equals(row[1])));
        assertTrue(courseCounts.stream().anyMatch(row -> ((Course) row[0]).getTitle().equals("Java Programming") && ((Long) row[1]) == 2L));
        assertTrue(courseCounts.stream().anyMatch(row -> ((Course) row[0]).getTitle().equals("Database Systems") && ((Long) row[1]) == 0L));
        assertTrue(courseCounts.stream().anyMatch(row -> ((Course) row[0]).getTitle().equals("Web Development") && ((Long) row[1]) == 1L));
    }
}

