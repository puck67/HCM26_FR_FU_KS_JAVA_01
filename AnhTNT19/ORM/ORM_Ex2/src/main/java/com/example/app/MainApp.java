package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class MainApp {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   Training Center Management System  ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> studentMenu();
                case 2 -> courseMenu();
                case 3 -> enrollmentMenu();
                case 4 -> queriesMenu();
                case 5 -> {
                    System.out.println("\nGoodbye!");
                    running = false;
                }
                default -> System.out.println("[!] Invalid option. Please try again.");
            }
        }

        HibernateUtil.shutdown();
        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n══════════════ MAIN MENU ══════════════");
        System.out.println("  1. Student Management");
        System.out.println("  2. Course Management");
        System.out.println("  3. Enrollment Management");
        System.out.println("  4. Queries and Reports");
        System.out.println("  5. Exit");
        System.out.println("═══════════════════════════════════════");
    }

    private static void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n────── Student Management ──────");
            System.out.println("  1. Create a new student");
            System.out.println("  2. Update student information");
            System.out.println("  3. Delete a student");
            System.out.println("  4. View student by ID");
            System.out.println("  5. List all students");
            System.out.println("  6. Back to main menu");
            System.out.println("─────────────────────────────────");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> createStudent();
                case 2 -> updateStudent();
                case 3 -> deleteStudent();
                case 4 -> viewStudentById();
                case 5 -> listAllStudents();
                case 6 -> back = true;
                default -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void createStudent() {
        System.out.println("\n-- Create New Student --");
        String name = readString("Enter name: ");
        int age = readInt("Enter age: ");
        try {
            Student s = studentService.createStudent(name, age);
            System.out.println("[✓] Created: " + s);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        System.out.println("\n-- Update Student --");
        int id = readInt("Enter student ID: ");
        try {
            Student existing = studentService.getStudentById(id);
            System.out.println("Current: " + existing);
            String name = readString("New name (current: " + existing.getName() + "): ");
            int age = readInt("New age (current: " + existing.getAge() + "): ");
            studentService.updateStudent(id, name, age);
            System.out.println("[✓] Updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        System.out.println("\n-- Delete Student --");
        int id = readInt("Enter student ID to delete: ");
        try {
            Student s = studentService.getStudentById(id);
            System.out.println("About to delete: " + s);
            String confirm = readString("Confirm? (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                studentService.deleteStudent(id);
                System.out.println("[✓] Student deleted.");
            } else {
                System.out.println("[i] Cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void viewStudentById() {
        System.out.println("\n-- View Student by ID --");
        int id = readInt("Enter student ID: ");
        try {
            Student s = studentService.getStudentById(id);
            System.out.println("Found: " + s);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    private static void listAllStudents() {
        System.out.println("\n-- All Students --");
        List<Student> list = studentService.getAllStudents();
        if (list.isEmpty()) {
            System.out.println("[i] No students found.");
        } else {
            list.forEach(s -> System.out.println("  " + s));
            System.out.println("Total: " + list.size() + " student(s).");
        }
    }

    private static void courseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n────── Course Management ──────");
            System.out.println("  1. Create a new course");
            System.out.println("  2. Update course information");
            System.out.println("  3. Delete a course");
            System.out.println("  4. View course by ID");
            System.out.println("  5. List all courses");
            System.out.println("  6. Back to main menu");
            System.out.println("───────────────────────────────");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> createCourse();
                case 2 -> updateCourse();
                case 3 -> deleteCourse();
                case 4 -> viewCourseById();
                case 5 -> listAllCourses();
                case 6 -> back = true;
                default -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void createCourse() {
        System.out.println("\n-- Create New Course --");
        String title = readString("Enter title: ");
        int credit = readInt("Enter credit: ");
        try {
            Course c = courseService.createCourse(title, credit);
            System.out.println("[✓] Created: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void updateCourse() {
        System.out.println("\n-- Update Course --");
        int id = readInt("Enter course ID: ");
        try {
            Course existing = courseService.getCourseById(id);
            System.out.println("Current: " + existing);
            String title = readString("New title (current: " + existing.getTitle() + "): ");
            int credit = readInt("New credit (current: " + existing.getCredit() + "): ");
            courseService.updateCourse(id, title, credit);
            System.out.println("[✓] Updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void deleteCourse() {
        System.out.println("\n-- Delete Course --");
        int id = readInt("Enter course ID to delete: ");
        try {
            Course c = courseService.getCourseById(id);
            System.out.println("About to delete: " + c);
            String confirm = readString("Confirm? (yes/no): ");
            if (confirm.equalsIgnoreCase("yes")) {
                courseService.deleteCourse(id);
                System.out.println("[✓] Course deleted.");
            } else {
                System.out.println("[i] Cancelled.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void viewCourseById() {
        System.out.println("\n-- View Course by ID --");
        int id = readInt("Enter course ID: ");
        try {
            Course c = courseService.getCourseById(id);
            System.out.println("Found: " + c);
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    private static void listAllCourses() {
        System.out.println("\n-- All Courses --");
        List<Course> list = courseService.getAllCourses();
        if (list.isEmpty()) {
            System.out.println("[i] No courses found.");
        } else {
            list.forEach(c -> System.out.println("  " + c));
            System.out.println("Total: " + list.size() + " course(s).");
        }
    }

    private static void enrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n────── Enrollment Management ──────");
            System.out.println("  1. Enroll a student in a course");
            System.out.println("  2. Remove a student from a course");
            System.out.println("  3. View courses of a student");
            System.out.println("  4. View students of a course");
            System.out.println("  5. Back to main menu");
            System.out.println("────────────────────────────────────");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> enrollStudent();
                case 2 -> removeEnrollment();
                case 3 -> viewCoursesOfStudent();
                case 4 -> viewStudentsOfCourse();
                case 5 -> back = true;
                default -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void enrollStudent() {
        System.out.println("\n-- Enroll Student in Course --");
        int studentId = readInt("Enter student ID: ");
        int courseId  = readInt("Enter course ID: ");
        try {
            studentService.enrollStudentInCourse(studentId, courseId);
            System.out.println("[✓] Enrollment successful.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void removeEnrollment() {
        System.out.println("\n-- Remove Student from Course --");
        int studentId = readInt("Enter student ID: ");
        int courseId  = readInt("Enter course ID: ");
        try {
            studentService.removeStudentFromCourse(studentId, courseId);
            System.out.println("[✓] Removed successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("[!] Error: " + e.getMessage());
        }
    }

    private static void viewCoursesOfStudent() {
        System.out.println("\n-- Courses of Student --");
        int studentId = readInt("Enter student ID: ");
        try {
            List<Course> courses = studentService.getCoursesOfStudent(studentId);
            if (courses.isEmpty()) {
                System.out.println("[i] Student is not enrolled in any course.");
            } else {
                System.out.println("Enrolled courses:");
                courses.forEach(c -> System.out.println("  - " + c));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    private static void viewStudentsOfCourse() {
        System.out.println("\n-- Students of Course --");
        int courseId = readInt("Enter course ID: ");
        try {
            List<Student> students = courseService.getStudentsOfCourse(courseId);
            if (students.isEmpty()) {
                System.out.println("[i] No students enrolled in this course.");
            } else {
                System.out.println("Enrolled students:");
                students.forEach(s -> System.out.println("  - " + s));
            }
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    private static void queriesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n────── Queries and Reports ──────");
            System.out.println("  1. Find students older than a given age     [HQL]");
            System.out.println("  2. Find students by name                    [Named Query]");
            System.out.println("  3. List students and their courses          [HQL Join]");
            System.out.println("  4. Find courses with credit greater than N  [Criteria API]");
            System.out.println("  5. Count students in each course            [Aggregation]");
            System.out.println("  6. Find students enrolled in a course       [Parameterized]");
            System.out.println("  7. Back to main menu");
            System.out.println("─────────────────────────────────────────────");

            int choice = readInt("Select option: ");
            switch (choice) {
                case 1 -> queryOlderThan();
                case 2 -> queryByName();
                case 3 -> queryStudentsWithCourses();
                case 4 -> queryCoursesByCredit();
                case 5 -> queryCountPerCourse();
                case 6 -> queryStudentsByCourse();
                case 7 -> back = true;
                default -> System.out.println("[!] Invalid option.");
            }
        }
    }

    private static void queryOlderThan() {
        int age = readInt("Find students older than age: ");
        List<Student> result = studentService.findStudentsOlderThan(age);
        System.out.println("\nStudents older than " + age + ":");
        if (result.isEmpty()) System.out.println("  [i] No results.");
        else result.forEach(s -> System.out.println("  " + s));
    }

    private static void queryByName() {
        String name = readString("Enter name to search (partial match supported): ");
        List<Student> result = studentService.findStudentsByName(name);
        System.out.println("\nResults for '" + name + "':");
        if (result.isEmpty()) System.out.println("  [i] No results.");
        else result.forEach(s -> System.out.println("  " + s));
    }

    private static void queryStudentsWithCourses() {
        System.out.println("\nStudents and their enrolled courses:");
        List<Student> result = studentService.findStudentsWithCourses();
        if (result.isEmpty()) {
            System.out.println("  [i] No students found.");
        } else {
            for (Student s : result) {
                System.out.print("  " + s.getName() + " → ");
                if (s.getCourses().isEmpty()) {
                    System.out.println("(no courses)");
                } else {
                    s.getCourses().forEach(c -> System.out.print("[" + c.getTitle() + "] "));
                    System.out.println();
                }
            }
        }
    }

    private static void queryCoursesByCredit() {
        int credit = readInt("Find courses with credit greater than: ");
        List<Course> result = courseService.findCoursesWithCreditGreaterThan(credit);
        System.out.println("\nCourses with credit > " + credit + ":");
        if (result.isEmpty()) System.out.println("  [i] No results.");
        else result.forEach(c -> System.out.println("  " + c));
    }

    private static void queryCountPerCourse() {
        System.out.println("\nStudent count per course:");
        List<Object[]> result = courseService.countStudentsPerCourse();
        if (result.isEmpty()) {
            System.out.println("  [i] No courses found.");
        } else {
            result.forEach(row ->
                    System.out.printf("  Course: %-25s | Students enrolled: %d%n", row[0], ((Number) row[1]).intValue())
            );
        }
    }

    private static void queryStudentsByCourse() {
        int courseId = readInt("Enter course ID: ");
        try {
            List<Student> result = studentService.findStudentsByCourse(courseId);
            System.out.println("\nStudents in course ID " + courseId + ":");
            if (result.isEmpty()) System.out.println("  [i] No students enrolled.");
            else result.forEach(s -> System.out.println("  " + s));
        } catch (IllegalArgumentException e) {
            System.out.println("[!] " + e.getMessage());
        }
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }
}