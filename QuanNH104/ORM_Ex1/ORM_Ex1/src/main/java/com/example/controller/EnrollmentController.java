package com.example.controller;

import com.example.model.Course;
import com.example.model.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.view.CourseView;
import com.example.view.StudentView;

import java.util.Scanner;

public class EnrollmentController {
    private final StudentService studentService;
    private final CourseService courseService;
    private final StudentView studentView;
    private final CourseView courseView;

    public EnrollmentController(StudentService studentService, CourseService courseService, StudentView studentView, CourseView courseView) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.studentView = studentView;
        this.courseView = courseView;
    }

    public void enroll(Scanner scanner) {
        System.out.println("\n--- ENROLL STUDENT IN COURSE ---");
        try {
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine().trim());

            studentService.enrollStudentInCourse(studentId, courseId);
            System.out.println("Successfully enrolled Student in Course!");
        } catch (NumberFormatException e) {
            System.out.println("Error: IDs must be numeric values!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void unenroll(Scanner scanner) {
        System.out.println("\n--- UNENROLL STUDENT FROM COURSE ---");
        try {
            System.out.print("Enter Student ID: ");
            int studentId = Integer.parseInt(scanner.nextLine().trim());

            System.out.print("Enter Course ID: ");
            int courseId = Integer.parseInt(scanner.nextLine().trim());

            studentService.removeStudentFromCourse(studentId, courseId);
            System.out.println("Successfully unenrolled Student from Course!");
        } catch (NumberFormatException e) {
            System.out.println("Error: IDs must be numeric values!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void displayCoursesOfStudent(Scanner scanner) {
        System.out.println("\n--- COURSES OF STUDENT ---");
        System.out.print("Enter Student ID: ");
        try {
            int studentId = Integer.parseInt(scanner.nextLine().trim());
            Student student = studentService.getStudent(studentId);
            var courses = studentService.getCoursesOfStudent(studentId);
            studentView.printStudentDetails(student, courses);
        } catch (NumberFormatException e) {
            System.out.println("Error: Student ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void displayStudentsOfCourse(Scanner scanner) {
        System.out.println("\n--- STUDENTS OF COURSE ---");
        System.out.print("Enter Course ID: ");
        try {
            int courseId = Integer.parseInt(scanner.nextLine().trim());
            Course course = courseService.getCourse(courseId);
            var students = courseService.getStudentsOfCourse(courseId);
            courseView.printCourseDetails(course, students);
        } catch (NumberFormatException e) {
            System.out.println("Error: Course ID must be an integer!");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listStudentsAndCourses() {
        try {
            var results = studentService.getStudentsAndTheirCourses();
            studentView.printStudentsAndCoursesJoin(results);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
