package com.example.ui;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.util.InputUtil;
import java.util.List;
import java.util.Scanner;

public class QueryUI {
    public static void handleMenu(Scanner scanner, StudentService studentService, CourseService courseService) {
        int choice;
        do {
            System.out.println("\n=========================================");
            System.out.println("        HIBERNATE QUERY PRACTICE         ");
            System.out.println("=========================================");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");
            System.out.println("=========================================");

            choice = InputUtil.readIntInRange(scanner, "Please enter your choice (1-7): ", 1, 7,
                    "Invalid choice! Please choose a number between 1 and 7.");

            switch (choice) {
                case 1 -> findStudentsOlderThan(scanner, studentService);
                case 2 -> findByNameNamedQuery(scanner, studentService);
                case 3 -> findStudentsAndCoursesHqlJoin(studentService);
                case 4 -> findCoursesWithCreditGreaterThan(scanner, courseService);
                case 5 -> countStudentsEnrolledInEachCourse(courseService);
                case 6 -> findStudentsEnrolledInCourse(scanner, studentService);
                case 7 -> System.out.println("Returning to Main Menu...");
            }
        } while (choice != 7);
    }

    private static void findStudentsOlderThan(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- HQL Query: Find all students older than age ---");
        int age = InputUtil.readIntInRange(scanner, "Enter Age to search students: ", 0, 120,
                "Age must be between 0 and 120.");
        List<Student> studentsOlder = studentService.findStudentsOlderThan(age);
        if (studentsOlder.isEmpty()) {
            System.out.println("No students found older than " + age + ".");
        } else {
            System.out.println("Students older than " + age + ":");
            for (Student s : studentsOlder) {
                System.out.println("  - " + s);
            }
        }
    }

    private static void findStudentsAndCoursesHqlJoin(StudentService studentService) {
        System.out.println("\n--- HQL with Join: List students and enrolled courses ---");
        List<Object[]> joinResults = studentService.findStudentsAndCoursesHqlJoin();
        if (joinResults.isEmpty()) {
            System.out.println("No enrollments found.");
        } else {
            System.out.println("Student and Course Enrollments:");
            for (Object[] row : joinResults) {
                String studentName = (String) row[0];
                String courseTitle = (String) row[1];
                System.out.println("  - Student: " + studentName + " | Course: " + courseTitle);
            }
        }
    }

    private static void findByNameNamedQuery(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Named Query: Find students by name ---");
        String name = InputUtil.readNonEmptyString(scanner, "Enter Student Name to search: ", "Name cannot be empty.");
        List<Student> studentsByName = studentService.findByNameNamedQuery(name);
        if (studentsByName.isEmpty()) {
            System.out.println("No students found with name \"" + name + "\".");
        } else {
            System.out.println("Students found:");
            for (Student s : studentsByName) {
                System.out.println("  - " + s);
            }
        }
    }

    private static void findCoursesWithCreditGreaterThan(Scanner scanner, CourseService courseService) {
        System.out.println("\n--- Criteria API: Find courses with credit greater than value ---");
        int credit = InputUtil.readIntInRange(scanner, "Enter Credit to filter courses: ", 0, 10,
                "Credit must be between 0 and 10.");
        List<Course> coursesFilter = courseService.findCoursesWithCreditGreaterThan(credit);
        if (coursesFilter.isEmpty()) {
            System.out.println("No courses found with credit greater than " + credit + ".");
        } else {
            System.out.println("Courses with credit greater than " + credit + ":");
            for (Course c : coursesFilter) {
                System.out.println("  - " + c);
            }
        }
    }

    private static void countStudentsEnrolledInEachCourse(CourseService courseService) {
        System.out.println("\n--- Aggregation Query: Count students in each course ---");
        List<Object[]> aggResults = courseService.countStudentsEnrolledInEachCourse();
        if (aggResults.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Student counts per course:");
            for (Object[] row : aggResults) {
                String courseTitle = (String) row[0];
                Long count = (Long) row[1];
                System.out.println("  - Course: " + courseTitle + " | Enrolled Students: " + count);
            }
        }
    }

    private static void findStudentsEnrolledInCourse(Scanner scanner, StudentService studentService) {
        System.out.println("\n--- Parameterized HQL Query: Find students enrolled in specific course ---");
        int courseId = InputUtil.readInt(scanner, "Enter Course ID: ", "Course ID must be an integer.");
        List<Student> students = studentService.findStudentsEnrolledInCourse(courseId);
        if (students.isEmpty()) {
            System.out.println("No students found enrolled in course ID " + courseId + ".");
        } else {
            System.out.println("Students enrolled in course ID " + courseId + ":");
            for (Student s : students) {
                System.out.println("  - " + s);
            }
        }
    }
}
