package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.ConsoleUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class MenuController {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void handleStudentManagement(Scanner scanner) {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n*** Student Management ***");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");

            int choice = ConsoleUtil.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        String name = ConsoleUtil.readName(scanner, "Enter name: ");
                        int age = ConsoleUtil.readAge(scanner, "Enter age: ");
                        Student student = studentService.createStudent(name, age);
                        System.out.println("Student created successfully: " + student);
                    }
                    case 2 -> {
                        int id = ConsoleUtil.readPositiveInt(scanner, "Enter student ID to update: ");
                        String name = ConsoleUtil.readName(scanner, "Enter new name: ");
                        int age = ConsoleUtil.readAge(scanner, "Enter new age: ");
                        studentService.updateStudent(id, name, age);
                        System.out.println("Student updated successfully.");
                    }
                    case 3 -> {
                        int id = ConsoleUtil.readPositiveInt(scanner, "Enter student ID to delete: ");
                        studentService.deleteStudent(id);
                        System.out.println("Student deleted successfully.");
                    }
                    case 4 -> {
                        int id = ConsoleUtil.readPositiveInt(scanner, "Enter student ID to view: ");
                        Student student = studentService.getStudentById(id);
                        if (student != null) {
                            System.out.println(student);
                        } else {
                            System.out.println("Student not found with ID: " + id);
                        }
                    }
                    case 5 -> {
                        List<Student> students = studentService.getAllStudents();
                        if (students.isEmpty()) {
                            System.out.println("No students found.");
                        } else {
                            students.forEach(System.out::println);
                        }
                    }
                    case 6 -> inSubmenu = false;
                    default -> System.out.println("Invalid option. Please choose 1-6.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void handleCourseManagement(Scanner scanner) {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n*** Course Management ***");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");

            int choice = ConsoleUtil.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        String title = ConsoleUtil.readCourseTitle(scanner, "Enter course title: ");
                        int credit = ConsoleUtil.readCredit(scanner, "Enter credit: ");
                        Course course = courseService.createCourse(title, credit);
                        System.out.println("Course created successfully: " + course);
                    }
                    case 2 -> {
                        int id = ConsoleUtil.readPositiveInt(scanner, "Enter course ID to update: ");
                        String title = ConsoleUtil.readCourseTitle(scanner, "Enter new title: ");
                        int credit = ConsoleUtil.readCredit(scanner, "Enter new credit: ");
                        courseService.updateCourse(id, title, credit);
                        System.out.println("Course updated successfully.");
                    }
                    case 3 -> {
                        int id = ConsoleUtil.readPositiveInt(scanner, "Enter course ID to delete: ");
                        courseService.deleteCourse(id);
                        System.out.println("Course deleted successfully.");
                    }
                    case 4 -> {
                        int id = ConsoleUtil.readPositiveInt(scanner, "Enter course ID to view: ");
                        Course course = courseService.getCourseById(id);
                        if (course != null) {
                            System.out.println(course);
                        } else {
                            System.out.println("Course not found with ID: " + id);
                        }
                    }
                    case 5 -> {
                        List<Course> courses = courseService.getAllCourses();
                        if (courses.isEmpty()) {
                            System.out.println("No courses found.");
                        } else {
                            courses.forEach(System.out::println);
                        }
                    }
                    case 6 -> inSubmenu = false;
                    default -> System.out.println("Invalid option. Please choose 1-6.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void handleEnrollmentManagement(Scanner scanner) {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n*** Enrollment Management ***");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");

            int choice = ConsoleUtil.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        int studentId = ConsoleUtil.readPositiveInt(scanner, "Enter student ID: ");
                        int courseId = ConsoleUtil.readPositiveInt(scanner, "Enter course ID: ");
                        studentService.enrollStudentInCourse(studentId, courseId);
                        System.out.println("Successfully enrolled student in course.");
                    }
                    case 2 -> {
                        int studentId = ConsoleUtil.readPositiveInt(scanner, "Enter student ID: ");
                        int courseId = ConsoleUtil.readPositiveInt(scanner, "Enter course ID: ");
                        studentService.removeStudentFromCourse(studentId, courseId);
                        System.out.println("Successfully removed student from course.");
                    }
                    case 3 -> {
                        int studentId = ConsoleUtil.readPositiveInt(scanner, "Enter student ID: ");
                        Set<Course> courses = studentService.getCoursesOfStudent(studentId);
                        if (courses.isEmpty()) {
                            System.out.println("Student is not enrolled in any courses.");
                        } else {
                            System.out.println("Courses for Student ID " + studentId + ":");
                            courses.forEach(System.out::println);
                        }
                    }
                    case 4 -> {
                        int courseId = ConsoleUtil.readPositiveInt(scanner, "Enter course ID: ");
                        Set<Student> students = courseService.getStudentsOfCourse(courseId);
                        if (students.isEmpty()) {
                            System.out.println("No students enrolled in this course.");
                        } else {
                            System.out.println("Students in Course ID " + courseId + ":");
                            students.forEach(System.out::println);
                        }
                    }
                    case 5 -> inSubmenu = false;
                    default -> System.out.println("Invalid option. Please choose 1-5.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }

    public static void handleQueriesAndReports(Scanner scanner) {
        boolean inSubmenu = true;
        while (inSubmenu) {
            System.out.println("\n*** Queries and Reports ***");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");

            int choice = ConsoleUtil.readInt(scanner, "Enter your choice: ");
            try {
                switch (choice) {
                    case 1 -> {
                        int age = ConsoleUtil.readAge(scanner, "Enter age: ");
                        List<Student> students = studentService.findOlderThan(age);
                        if (students.isEmpty()) {
                            System.out.println("No students found older than " + age);
                        } else {
                            students.forEach(System.out::println);
                        }
                    }
                    case 2 -> {
                        String name = ConsoleUtil.readName(scanner, "Enter student name: ");
                        List<Student> students = studentService.findByName(name);
                        if (students.isEmpty()) {
                            System.out.println("No students found with name: " + name);
                        } else {
                            students.forEach(System.out::println);
                        }
                    }
                    case 3 -> {
                        List<Object[]> results = studentService.findStudentsWithCourseTitles();
                        if (results.isEmpty()) {
                            System.out.println("No enrollments found.");
                        } else {
                            results.forEach(row -> System.out.println("Student: " + row[0] + " | Course: " + row[1]));
                        }
                    }
                    case 4 -> {
                        int credit = ConsoleUtil.readCredit(scanner, "Enter credit threshold: ");
                        List<Course> courses = courseService.findCoursesWithCreditGreaterThan(credit);
                        if (courses.isEmpty()) {
                            System.out.println("No courses found with credit greater than " + credit);
                        } else {
                            courses.forEach(System.out::println);
                        }
                    }
                    case 5 -> {
                        List<Object[]> results = courseService.countStudentsPerCourse();
                        if (results.isEmpty()) {
                            System.out.println("No courses found.");
                        } else {
                            results.forEach(row -> System.out.println("Course: " + row[0] + " | Students enrolled: " + row[1]));
                        }
                    }
                    case 6 -> {
                        int courseId = ConsoleUtil.readPositiveInt(scanner, "Enter course ID: ");
                        List<Student> students = studentService.findStudentsByCourseId(courseId);
                        if (students.isEmpty()) {
                            System.out.println("No students found enrolled in course ID " + courseId);
                        } else {
                            students.forEach(System.out::println);
                        }
                    }
                    case 7 -> inSubmenu = false;
                    default -> System.out.println("Invalid option. Please choose 1-7.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
