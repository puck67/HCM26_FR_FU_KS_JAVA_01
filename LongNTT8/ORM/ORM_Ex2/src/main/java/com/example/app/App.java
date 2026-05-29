package com.example.app;

import com.example.util.HibernateUtil;
import com.example.util.Menu;

import java.util.Scanner;

public class App {
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Disable java.util.logging (Hibernate's fallback logger)
        java.util.logging.LogManager.getLogManager().reset();

        System.out.println("Initializing system, please wait...");
        // Force Hibernate to initialize before the menu
        HibernateUtil.getSessionFactory();
        System.out.println("System initialized successfully!\n");

        // Initialize Handlers
        StudentHandler studentHandler = new StudentHandler(scanner);
        CourseHandler courseHandler = new CourseHandler(scanner);
        EnrollmentHandler enrollmentHandler = new EnrollmentHandler(scanner);
        ReportHandler reportHandler = new ReportHandler(scanner);

        // Build Menus
        Menu studentMenu = new Menu("Student Management");
        studentMenu.addOption("1", "Create a new student", studentHandler::createStudent);
        studentMenu.addOption("2", "Update student information", studentHandler::updateStudent);
        studentMenu.addOption("3", "Delete a student", studentHandler::deleteStudent);
        studentMenu.addOption("4", "View student by ID", studentHandler::viewStudentById);
        studentMenu.addOption("5", "List all students", studentHandler::listAllStudents);

        Menu courseMenu = new Menu("Course Management");
        courseMenu.addOption("1", "Create a new course", courseHandler::createCourse);
        courseMenu.addOption("2", "Update course information", courseHandler::updateCourse);
        courseMenu.addOption("3", "Delete a course", courseHandler::deleteCourse);
        courseMenu.addOption("4", "View course by ID", courseHandler::viewCourseById);
        courseMenu.addOption("5", "List all courses", courseHandler::listAllCourses);

        Menu enrollmentMenu = new Menu("Enrollment Management");
        enrollmentMenu.addOption("1", "Enroll a student in a course", enrollmentHandler::enrollStudent);
        enrollmentMenu.addOption("2", "Remove a student from a course", enrollmentHandler::removeStudentFromCourse);
        enrollmentMenu.addOption("3", "View courses of a student", enrollmentHandler::viewCoursesOfStudent);
        enrollmentMenu.addOption("4", "View students of a course", enrollmentHandler::viewStudentsOfCourse);

        Menu queryMenu = new Menu("Queries and Reports");
        queryMenu.addOption("1", "Find students older than a given age (HQL)", reportHandler::queryOlderThan);
        queryMenu.addOption("2", "Find students by name (Named Query)", reportHandler::queryByName);
        queryMenu.addOption("3", "List students and their courses (HQL Join)", reportHandler::queryStudentsWithCourses);
        queryMenu.addOption("4", "Find courses with credit greater than a value (Criteria API)", reportHandler::queryCourseCredit);
        queryMenu.addOption("5", "Count number of students in each course (Aggregation Query)", reportHandler::queryCountStudents);
        queryMenu.addOption("6", "Find students enrolled in a specific course", reportHandler::queryStudentsByCourseId);

        Menu mainMenu = new Menu("Main Menu");
        mainMenu.addOption("1", "Student Management", () -> studentMenu.run(scanner));
        mainMenu.addOption("2", "Course Management", () -> courseMenu.run(scanner));
        mainMenu.addOption("3", "Enrollment Management", () -> enrollmentMenu.run(scanner));
        mainMenu.addOption("4", "Queries and Reports", () -> queryMenu.run(scanner));

        // Start application
        mainMenu.run(scanner);

        System.out.println("Exiting application...");
        HibernateUtil.shutdown();
        System.exit(0);
    }
}
