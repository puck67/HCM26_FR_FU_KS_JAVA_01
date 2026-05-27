package com.example.controller;

import com.example.model.Course;
import com.example.model.Student;
import com.example.service.StudentService;

import java.util.Scanner;

public class EnrollmentController {
    private final StudentService studentService;

    public EnrollmentController(StudentService studentService) {
        this.studentService = studentService;
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
}
