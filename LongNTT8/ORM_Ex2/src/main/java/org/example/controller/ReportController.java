package org.example.controller;

import org.example.dao.CourseDAO;
import org.example.dao.StudentDAO;

public class ReportController {
    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    public void runQueryDemos() {
        System.out.println("--- Queries ---");
        System.out.println("1. Age > 20:");
        studentDAO.findStudentsOlderThan(20).forEach(System.out::println);

        System.out.println("\n2. Students & Courses:");
        studentDAO.findStudentsWithCourses().forEach(s -> System.out.println(s + " -> " + s.getCourses()));

        System.out.println("\n3. Name 'John':");
        studentDAO.findStudentsByName("John").forEach(System.out::println);

        System.out.println("\n4. Credit > 3:");
        courseDAO.findCoursesWithCreditGreaterThan(3).forEach(System.out::println);

        System.out.println("\n5. Student count/course:");
        courseDAO.countStudentsPerCourse().forEach(row -> System.out.println(row[0] + ": " + row[1]));

        System.out.println("\n--- Bonus ---");
        System.out.println("Paging p1:");
        studentDAO.findAllPaginated(1, 2).forEach(System.out::println);

        System.out.println("\nWithout courses:");
        studentDAO.findStudentsWithoutCourses().forEach(System.out::println);
    }
}
