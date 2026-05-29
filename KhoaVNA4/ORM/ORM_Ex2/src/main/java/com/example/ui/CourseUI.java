package com.example.ui;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.util.InputUtil;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class CourseUI {
    public static void handleMenu(Scanner scanner, CourseService courseService) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("             COURSE MANAGEMENT              ");
            System.out.println("=========================================");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-6): ", 1, 6,
                    "Invalid choice! Please choose a number between 1 and 6.");

            switch (choice) {
                case 1 -> addNewCourse(scanner, courseService);
                case 2 -> updateCourse(scanner, courseService);
                case 3 -> deleteCourse(scanner, courseService);
                case 4 -> searchCourseById(scanner, courseService);
                case 5 -> displayAllCourses(courseService);
                case 6 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 6);
    }

    private static void addNewCourse(Scanner scanner, CourseService courseService) {
        System.out.println("\n--- Add New Course ---");
        String courseTitle = InputUtil.readNonEmptyString(scanner, "Enter Course Title: ",
                "Course Title cannot be empty.");
        int courseCredit = InputUtil.readIntInRange(scanner, "Enter Course Credit: ", 1, 10,
                "Course Credit must be between 1 and 10.");

        courseService.createCourse(courseTitle, courseCredit);
        System.out.println("Course added successfully!");
    }

    private static void displayAllCourses(CourseService courseService) {
        System.out.println("\n--- Display All Courses ---");
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            for (Course c : courses) {
                System.out.println(c);
                Set<Student> students = c.getStudents();
                if (students != null && !students.isEmpty()) {
                    System.out.print("  Enrolled Students: ");
                    for (Student s : students) {
                        System.out.print(s.getName() + " (ID: " + s.getId() + ") | ");
                    }
                    System.out.println();
                }
            }
        }
    }

    private static void searchCourseById(Scanner scanner, CourseService courseService) {
        System.out.println("\n--- Search Course by ID ---");
        int id = InputUtil.readInt(scanner, "Enter Course ID to search: ", "ID must be an integer.");
        Course course = courseService.getCourseById(id);
        if (course != null) {
            System.out.println("Course found: " + course);
            Set<Student> students = course.getStudents();
            if (students != null && !students.isEmpty()) {
                System.out.println("Enrolled Students:");
                for (Student s : students) {
                    System.out.println("  - " + s);
                }
            } else {
                System.out.println("No students enrolled.");
            }
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }

    private static void updateCourse(Scanner scanner, CourseService courseService) {
        System.out.println("\n--- Update Course ---");
        int id = InputUtil.readInt(scanner, "Enter Course ID to update: ", "ID must be an integer.");
        Course course = courseService.getCourseById(id);
        if (course != null) {
            System.out.println("Current details: " + course);
            String newTitle = InputUtil.readStringOrKeep(scanner, "Enter new Title", course.getTitle());

            int newCredit = course.getCredit();
            while (true) {
                System.out.print("Enter new Credit (leave empty to keep '" + course.getCredit() + "'): ");
                String creditInput = scanner.nextLine().trim();
                if (creditInput.isEmpty()) {
                    break;
                }
                try {
                    int creditVal = Integer.parseInt(creditInput);
                    if (creditVal >= 1 && creditVal <= 10) {
                        newCredit = creditVal;
                        break;
                    } else {
                        System.out.println("Credit must be between 1 and 10.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Credit must be an integer.");
                }
            }

            courseService.updateCourse(id, newTitle, newCredit);
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }

    private static void deleteCourse(Scanner scanner, CourseService courseService) {
        System.out.println("\n--- Delete Course ---");
        int id = InputUtil.readInt(scanner, "Enter Course ID to delete: ", "ID must be an integer.");
        Course course = courseService.getCourseById(id);
        if (course != null) {
            courseService.deleteCourse(id);
            System.out.println("Course deleted successfully!");
        } else {
            System.out.println("Course with ID " + id + " not found.");
        }
    }
}
