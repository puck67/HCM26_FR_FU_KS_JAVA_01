package com.example.ui;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.util.InputUtil;
import java.util.Scanner;
import java.util.Set;

public class EnrollmentUI {
    public static void handleMenu(Scanner scanner, StudentService studentService, CourseService courseService) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("            ENROLLMENT SYSTEM            ");
            System.out.println("=========================================");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-5): ", 1, 5,
                    "Invalid choice! Please choose a number between 1 and 5.");

            switch (choice) {
                case 1 -> enrollStudent(scanner, studentService, courseService);
                case 2 -> unenrollStudent(scanner, studentService, courseService);
                case 3 -> displayCoursesOfStudent(scanner, studentService);
                case 4 -> displayStudentsOfCourse(scanner, courseService);
                case 5 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 5);
    }

    private static void enrollStudent(Scanner scanner, StudentService studentService, CourseService courseService) {
        System.out.println("\n--- Enroll Student in Course ---");
        int studentId = InputUtil.readInt(scanner, "Enter Student ID: ", "Student ID must be an integer.");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        if (student.getCourses().contains(course)) {
            System.out.println("Student is already enrolled in this course!");
            return;
        }

        studentService.enrollStudentInCourse(studentId, courseId);
        System.out.println("Student enrolled in course successfully!");
    }

    private static void unenrollStudent(Scanner scanner, StudentService studentService, CourseService courseService) {
        System.out.println("\n--- Remove Student from Course ---");
        int studentId = InputUtil.readInt(scanner, "Enter Student ID: ", "Student ID must be an integer.");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        if (!student.getCourses().contains(course)) {
            System.out.println("Student is not enrolled in this course!");
            return;
        }

        studentService.removeStudentFromCourse(studentId, courseId);
        System.out.println("Student removed from course successfully!");
    }

    private static void displayCoursesOfStudent(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Display All Courses of a Student ---");
        int studentId = InputUtil.readInt(scanner, "Enter Student ID: ", "Student ID must be an integer.");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found!");
            return;
        }

        System.out.println(
                "Student: " + student.getName() + " (ID: " + student.getId() + ", Age: " + student.getAge() + ")");
        Set<Course> courses = studentService.getCoursesOfStudent(studentId);
        if (courses == null || courses.isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            System.out.println("Enrolled Courses:");
            for (Course c : courses) {
                System.out.println("  - " + c.getTitle() + " (ID: " + c.getId() + ", Credits: " + c.getCredit() + ")");
            }
        }
    }

    private static void displayStudentsOfCourse(Scanner scanner, CourseService courseService) {
        System.out.println("\n--- Display All Students of a Course ---");
        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found!");
            return;
        }

        System.out.println(
                "Course: " + course.getTitle() + " (ID: " + course.getId() + ", Credits: " + course.getCredit() + ")");
        Set<Student> students = courseService.getStudentsOfCourse(courseId);
        if (students == null || students.isEmpty()) {
            System.out.println("No students enrolled.");
        } else {
            System.out.println("Enrolled Students:");
            for (Student s : students) {
                System.out.println("  - " + s.getName() + " (ID: " + s.getId() + ", Age: " + s.getAge() + ")");
            }
        }
    }
}
