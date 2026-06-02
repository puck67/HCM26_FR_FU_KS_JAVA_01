package com.hibernate.practice;

import com.hibernate.practice.dao.CourseDAO;
import com.hibernate.practice.dao.StudentDAO;
import com.hibernate.practice.model.Course;
import com.hibernate.practice.model.Student;
import com.hibernate.practice.util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;
        System.out.println("==========================================================");
        System.out.println("               HIBERNATE MANAGEMENT SYSTEM                ");
        System.out.println("==========================================================");

        while (!exit) {
            printMainMenu();
            System.out.print("Enter your choice: ");
            int choice = readChoice();

            switch (choice) {
                case 1 -> handleStudentMenu();
                case 2 -> handleCourseMenu();
                case 3 -> handleEnrollmentMenu();
                case 4 -> handleQueryMenu();
                case 5 -> preloadSampleData();
                case 0 -> {
                    exit = true;
                    System.out.println("Exiting the application. Goodbye!");
                }
                default -> System.out.println("Invalid choice. Try again.");
            }
            System.out.println();
        }

        HibernateUtil.shutdown();
        scanner.close();
    }

    private static int readChoice() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void printMainMenu() {
        System.out.println("\n----------------- MAIN MENU -----------------");
        System.out.println(" [1] Student Management");
        System.out.println(" [2] Course Management");
        System.out.println(" [3] Enrollment Management");
        System.out.println(" [4] Query & Practice Tasks");
        System.out.println(" [5] Preload Sample Data");
        System.out.println(" [0] Exit");
        System.out.println("---------------------------------------------");
    }

    private static void handleStudentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- STUDENT MANAGEMENT ---");
            System.out.println(" [1] Create Student");
            System.out.println(" [2] Update Student");
            System.out.println(" [3] Delete Student");
            System.out.println(" [4] Find Student by ID");
            System.out.println(" [5] List All Students");
            System.out.println(" [0] Back to Main Menu");
            System.out.print("Enter option: ");
            int option = readChoice();

            switch (option) {
                case 1 -> handleCreateStudent();
                case 2 -> handleUpdateStudent();
                case 3 -> handleDeleteStudent();
                case 4 -> handleGetStudentById();
                case 5 -> handleListAllStudents();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void handleCourseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- COURSE MANAGEMENT ---");
            System.out.println(" [1] Create Course");
            System.out.println(" [2] Update Course");
            System.out.println(" [3] Delete Course");
            System.out.println(" [4] List All Courses");
            System.out.println(" [0] Back to Main Menu");
            System.out.print("Enter option: ");
            int option = readChoice();

            switch (option) {
                case 1 -> handleCreateCourse();
                case 2 -> handleUpdateCourse();
                case 3 -> handleDeleteCourse();
                case 4 -> handleListAllCourses();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void handleEnrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- ENROLLMENT MANAGEMENT ---");
            System.out.println(" [1] Enroll Student in Course");
            System.out.println(" [2] Remove Student from Course");
            System.out.println(" [3] List Courses of Student");
            System.out.println(" [4] List Students of Course");
            System.out.println(" [0] Back to Main Menu");
            System.out.print("Enter option: ");
            int option = readChoice();

            switch (option) {
                case 1 -> handleEnrollStudent();
                case 2 -> handleRemoveStudentFromCourse();
                case 3 -> handleDisplayCoursesOfStudent();
                case 4 -> handleDisplayStudentsOfCourse();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void handleQueryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- QUERY & PRACTICE TASKS ---");
            System.out.println(" [1] HQL: Students Older Than Age");
            System.out.println(" [2] HQL Join: Students and their Courses");
            System.out.println(" [3] Named Query: Find Student by Name");
            System.out.println(" [4] Criteria API: Courses with Credits > Value");
            System.out.println(" [5] Aggregation: Student Count per Course");
            System.out.println(" [0] Back to Main Menu");
            System.out.print("Enter option: ");
            int option = readChoice();

            switch (option) {
                case 1 -> handleQueryStudentsOlderThan();
                case 2 -> handleQueryStudentsAndCourses();
                case 3 -> handleNamedQueryStudentByName();
                case 4 -> handleCriteriaQueryCoursesByCredit();
                case 5 -> handleAggregationStudentCount();
                case 0 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    private static void preloadSampleData() {
        System.out.println("Preloading sample data...");
        Course c1 = new Course("Java Programming", 3);
        Course c2 = new Course("Database Systems", 4);
        Course c3 = new Course("Web Development", 3);
        courseDAO.saveCourse(c1);
        courseDAO.saveCourse(c2);
        courseDAO.saveCourse(c3);

        Student s1 = new Student("John", 19);
        Student s2 = new Student("Anna", 22);
        Student s3 = new Student("Mike", 25);
        Student s4 = new Student("Alex", 18);
        studentDAO.saveStudent(s1);
        studentDAO.saveStudent(s2);
        studentDAO.saveStudent(s3);
        studentDAO.saveStudent(s4);

        studentDAO.enrollStudentInCourse(s1.getId(), c1.getId());
        studentDAO.enrollStudentInCourse(s1.getId(), c2.getId());
        studentDAO.enrollStudentInCourse(s2.getId(), c1.getId());
        studentDAO.enrollStudentInCourse(s2.getId(), c3.getId());
        studentDAO.enrollStudentInCourse(s3.getId(), c2.getId());
        studentDAO.enrollStudentInCourse(s3.getId(), c3.getId());

        System.out.println("Sample data preloaded successfully.");
    }

    private static void handleCreateStudent() {
        System.out.print("Enter Student Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter Student Age: ");
        int age = readChoice();
        if (age < 0) {
            System.out.println("Invalid age.");
            return;
        }
        Student student = new Student(name, age);
        studentDAO.saveStudent(student);
        System.out.println("Student created: " + student);
    }

    private static void handleUpdateStudent() {
        System.out.print("Enter Student ID to update: ");
        int id = readChoice();
        Student student = studentDAO.getStudent(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.print("Enter New Name: ");
        String name = scanner.nextLine().trim();
        System.out.print("Enter New Age: ");
        int age = readChoice();
        if (age < 0) {
            System.out.println("Invalid age.");
            return;
        }

        student.setName(name);
        student.setAge(age);
        studentDAO.updateStudent(student);
        System.out.println("Student updated: " + student);
    }

    private static void handleDeleteStudent() {
        System.out.print("Enter Student ID to delete: ");
        int id = readChoice();
        studentDAO.deleteStudent(id);
        System.out.println("Operation processed.");
    }

    private static void handleGetStudentById() {
        System.out.print("Enter Student ID: ");
        int id = readChoice();
        Student student = studentDAO.getStudent(id);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Found: " + student);
        }
    }

    private static void handleListAllStudents() {
        List<Student> students = studentDAO.getAllStudents();
        students.forEach(System.out::println);
    }

    private static void handleCreateCourse() {
        System.out.print("Enter Course Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter Course Credits: ");
        int credit = readChoice();
        if (credit < 0) {
            System.out.println("Invalid credits.");
            return;
        }

        Course course = new Course(title, credit);
        courseDAO.saveCourse(course);
        System.out.println("Course created: " + course);
    }

    private static void handleUpdateCourse() {
        System.out.print("Enter Course ID to update: ");
        int id = readChoice();
        Course course = courseDAO.getCourse(id);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        System.out.print("Enter New Title: ");
        String title = scanner.nextLine().trim();
        System.out.print("Enter New Credits: ");
        int credit = readChoice();
        if (credit < 0) {
            System.out.println("Invalid credits.");
            return;
        }

        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.updateCourse(course);
        System.out.println("Course updated: " + course);
    }

    private static void handleDeleteCourse() {
        System.out.print("Enter Course ID to delete: ");
        int id = readChoice();
        courseDAO.deleteCourse(id);
        System.out.println("Operation processed.");
    }

    private static void handleListAllCourses() {
        List<Course> courses = courseDAO.getAllCourses();
        courses.forEach(System.out::println);
    }

    private static void handleEnrollStudent() {
        System.out.print("Enter Student ID: ");
        int sId = readChoice();
        System.out.print("Enter Course ID: ");
        int cId = readChoice();

        studentDAO.enrollStudentInCourse(sId, cId);
        System.out.println("Enrollment request processed.");
    }

    private static void handleRemoveStudentFromCourse() {
        System.out.print("Enter Student ID: ");
        int sId = readChoice();
        System.out.print("Enter Course ID: ");
        int cId = readChoice();

        studentDAO.removeStudentFromCourse(sId, cId);
        System.out.println("Removal request processed.");
    }

    private static void handleDisplayCoursesOfStudent() {
        System.out.print("Enter Student ID: ");
        int id = readChoice();
        List<Course> courses = studentDAO.getCoursesOfStudent(id);
        courses.forEach(System.out::println);
    }

    private static void handleDisplayStudentsOfCourse() {
        System.out.print("Enter Course ID: ");
        int id = readChoice();
        List<Student> students = studentDAO.getStudentsOfCourse(id);
        students.forEach(System.out::println);
    }

    private static void handleQueryStudentsOlderThan() {
        System.out.print("Enter age threshold: ");
        int age = readChoice();
        List<Student> students = studentDAO.findStudentsOlderThan(age);
        students.forEach(System.out::println);
    }

    private static void handleQueryStudentsAndCourses() {
        List<Student> list = studentDAO.getStudentsWithCourses();
        for (Student s : list) {
            System.out.print("  - " + s.getName() + ": ");
            if (s.getCourses().isEmpty()) {
                System.out.println("[No enrolled courses]");
            } else {
                StringBuilder sb = new StringBuilder();
                s.getCourses().forEach(c -> sb.append(c.getTitle()).append(", "));
                System.out.println(sb.substring(0, sb.length() - 2));
            }
        }
    }

    private static void handleNamedQueryStudentByName() {
        System.out.print("Enter student name to search: ");
        String name = scanner.nextLine().trim();
        List<Student> list = studentDAO.findStudentsByName(name);
        list.forEach(System.out::println);
    }

    private static void handleCriteriaQueryCoursesByCredit() {
        System.out.print("Enter minimum credits: ");
        int minCredit = readChoice();
        List<Course> list = courseDAO.findCoursesByCreditGreaterThan(minCredit);
        list.forEach(System.out::println);
    }

    private static void handleAggregationStudentCount() {
        List<Object[]> results = courseDAO.getStudentCountPerCourse();
        for (Object[] row : results) {
            System.out.println("  - " + row[0] + ": " + row[1] + " student(s)");
        }
    }

}
