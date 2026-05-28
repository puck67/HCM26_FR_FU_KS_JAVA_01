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
import java.util.Set;

public class MainApp {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            System.out.println("==================================================");
            System.out.println("  Initializing Hibernate SessionFactory...        ");
            System.out.println("==================================================");
            // Khởi tạo Hibernate SessionFactory
            HibernateUtil.getSessionFactory();
            


            boolean exit = false;
            while (!exit) {
                printMainMenu();
                int choice = readInt("Enter your choice: ");
                switch (choice) {
                    case 1 -> handleStudentMenu();
                    case 2 -> handleCourseMenu();
                    case 3 -> handleEnrollmentMenu();
                    case 4 -> handleQueriesMenu();
                    case 5 -> {
                        exit = true;
                        System.out.println("Exiting the application... Goodbye!");
                    }
                    default -> System.out.println("Invalid choice! Please enter a number between 1 and 5.");
                }
            }
        } finally {
            // Đóng SessionFactory và Scanner khi kết thúc
            HibernateUtil.shutdown();
            scanner.close();
        }
    }



    private static void printMainMenu() {
        System.out.println("==================== MAIN MENU ====================");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Queries and Reports");
        System.out.println("5. Exit");
        System.out.println("===================================================");
    }

    // --- QUẢN LÝ SINH VIÊN ---
    private static void handleStudentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Student Management Submenu ---");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1 -> {
                    String name = readString("Enter student name: ");
                    int age = readInt("Enter student age: ");
                    try {
                        Student created = studentService.createStudent(name, age);
                        System.out.println("Student created successfully: " + created);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Validation Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    int id = readInt("Enter student ID to update: ");
                    Student existing = studentService.getStudentById(id);
                    if (existing == null) {
                        System.out.println("Student not found!");
                    } else {
                        String newName = readString("Enter new name (" + existing.getName() + "): ");
                        int newAge = readInt("Enter new age (" + existing.getAge() + "): ");
                        try {
                            studentService.updateStudent(id, newName, newAge);
                            System.out.println("Student updated successfully!");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Validation Error: " + e.getMessage());
                        }
                    }
                }
                case 3 -> {
                    int delId = readInt("Enter student ID to delete: ");
                    if (studentService.getStudentById(delId) == null) {
                        System.out.println("Student not found!");
                    } else {
                        studentService.deleteStudent(delId);
                        System.out.println("Student deleted successfully!");
                    }
                }
                case 4 -> {
                    int viewId = readInt("Enter student ID: ");
                    Student s = studentService.getStudentById(viewId);
                    if (s != null) {
                        System.out.println("Student Details:");
                        System.out.println("ID: " + s.getId());
                        System.out.println("Name: " + s.getName());
                        System.out.println("Age: " + s.getAge());
                        System.out.println("Enrolled Courses: ");
                        if (s.getCourses().isEmpty()) {
                            System.out.println("  None");
                        } else {
                            for (Course c : s.getCourses()) {
                                System.out.println("  - [ID: " + c.getId() + "] " + c.getTitle() + " (" + c.getCredit() + " credits)");
                            }
                        }
                    } else {
                        System.out.println("Student not found!");
                    }
                }
                case 5 -> {
                    List<Student> students = studentService.getAllStudents();
                    System.out.println("\nAll Students List:");
                    if (students.isEmpty()) {
                        System.out.println("No students found.");
                    } else {
                        for (Student stud : students) {
                            System.out.print("[ID: " + stud.getId() + "] " + stud.getName() + " (Age: " + stud.getAge() + ") - Courses: ");
                            if (stud.getCourses().isEmpty()) {
                                System.out.println("None");
                            } else {
                                StringBuilder sb = new StringBuilder();
                                for (Course c : stud.getCourses()) {
                                    sb.append(c.getTitle()).append(", ");
                                }
                                System.out.println(sb.substring(0, sb.length() - 2));
                            }
                        }
                    }
                }
                case 6 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- QUẢN LÝ KHÓA HỌC ---
    private static void handleCourseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Course Management Submenu ---");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1 -> {
                    String title = readString("Enter course title: ");
                    int credit = readInt("Enter course credits: ");
                    try {
                        Course created = courseService.createCourse(title, credit);
                        System.out.println("Course created successfully: " + created);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Validation Error: " + e.getMessage());
                    }
                }
                case 2 -> {
                    int id = readInt("Enter course ID to update: ");
                    Course existing = courseService.getCourseById(id);
                    if (existing == null) {
                        System.out.println("Course not found!");
                    } else {
                        String newTitle = readString("Enter new title (" + existing.getTitle() + "): ");
                        int newCredit = readInt("Enter new credits (" + existing.getCredit() + "): ");
                        try {
                            courseService.updateCourse(id, newTitle, newCredit);
                            System.out.println("Course updated successfully!");
                        } catch (IllegalArgumentException e) {
                            System.out.println("Validation Error: " + e.getMessage());
                        }
                    }
                }
                case 3 -> {
                    int delId = readInt("Enter course ID to delete: ");
                    if (courseService.getCourseById(delId) == null) {
                        System.out.println("Course not found!");
                    } else {
                        courseService.deleteCourse(delId);
                        System.out.println("Course deleted successfully!");
                    }
                }
                case 4 -> {
                    int viewId = readInt("Enter course ID: ");
                    Course c = courseService.getCourseById(viewId);
                    if (c != null) {
                        System.out.println("Course Details:");
                        System.out.println("ID: " + c.getId());
                        System.out.println("Title: " + c.getTitle());
                        System.out.println("Credits: " + c.getCredit());
                        System.out.println("Enrolled Students: ");
                        if (c.getStudents().isEmpty()) {
                            System.out.println("  None");
                        } else {
                            for (Student s : c.getStudents()) {
                                System.out.println("  - [ID: " + s.getId() + "] " + s.getName() + " (Age: " + s.getAge() + ")");
                            }
                        }
                    } else {
                        System.out.println("Course not found!");
                    }
                }
                case 5 -> {
                    List<Course> courses = courseService.getAllCourses();
                    System.out.println("\nAll Courses List:");
                    if (courses.isEmpty()) {
                        System.out.println("No courses found.");
                    } else {
                        for (Course crs : courses) {
                            System.out.print("[ID: " + crs.getId() + "] " + crs.getTitle() + " (Credits: " + crs.getCredit() + ") - Students enrolled: ");
                            System.out.println(crs.getStudents().size());
                        }
                    }
                }
                case 6 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- QUẢN LÝ ĐĂNG KÝ HỌC ---
    private static void handleEnrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Enrollment Management Submenu ---");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1 -> {
                    int sId = readInt("Enter Student ID: ");
                    int cId = readInt("Enter Course ID: ");
                    if (studentService.getStudentById(sId) == null) {
                        System.out.println("Student not found!");
                    } else if (courseService.getCourseById(cId) == null) {
                        System.out.println("Course not found!");
                    } else {
                        studentService.enrollStudentInCourse(sId, cId);
                        System.out.println("Enrolled student successfully!");
                    }
                }
                case 2 -> {
                    int rsId = readInt("Enter Student ID: ");
                    int rcId = readInt("Enter Course ID: ");
                    if (studentService.getStudentById(rsId) == null) {
                        System.out.println("Student not found!");
                    } else if (courseService.getCourseById(rcId) == null) {
                        System.out.println("Course not found!");
                    } else {
                        studentService.removeStudentFromCourse(rsId, rcId);
                        System.out.println("Student removed from course successfully!");
                    }
                }
                case 3 -> {
                    int studId = readInt("Enter Student ID: ");
                    Set<Course> courses = studentService.getCoursesOfStudent(studId);
                    if (courses.isEmpty()) {
                        System.out.println("No courses found or student does not exist.");
                    } else {
                        System.out.println("Courses taken by student:");
                        for (Course crs : courses) {
                            System.out.println("- [ID: " + crs.getId() + "] " + crs.getTitle() + " (Credits: " + crs.getCredit() + ")");
                        }
                    }
                }
                case 4 -> {
                    int crsId = readInt("Enter Course ID: ");
                    Set<Student> students = courseService.getStudentsOfCourse(crsId);
                    if (students.isEmpty()) {
                        System.out.println("No students enrolled or course does not exist.");
                    } else {
                        System.out.println("Students enrolled in course:");
                        for (Student stud : students) {
                            System.out.println("- [ID: " + stud.getId() + "] " + stud.getName() + " (Age: " + stud.getAge() + ")");
                        }
                    }
                }
                case 5 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- TRUY VẤN VÀ BÁO CÁO ---
    private static void handleQueriesMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- Queries and Reports Submenu ---");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1 -> {
                    int age = readInt("Enter age threshold: ");
                    List<Student> olderStudents = studentService.getStudentsOlderThan(age);
                    System.out.println("Students older than " + age + ":");
                    if (olderStudents.isEmpty()) {
                        System.out.println("  None found.");
                    } else {
                        for (Student s : olderStudents) {
                            System.out.println("  - [ID: " + s.getId() + "] " + s.getName() + " (Age: " + s.getAge() + ")");
                        }
                    }
                }
                case 2 -> {
                    String name = readString("Enter name to search: ");
                    List<Student> namedStudents = studentService.getStudentsByName(name);
                    System.out.println("Students with name '" + name + "':");
                    if (namedStudents.isEmpty()) {
                        System.out.println("  None found.");
                    } else {
                        for (Student s : namedStudents) {
                            System.out.println("  - [ID: " + s.getId() + "] " + s.getName() + " (Age: " + s.getAge() + ")");
                        }
                    }
                }
                case 3 -> {
                    List<Object[]> joinResults = studentService.getStudentsWithCourseTitles();
                    System.out.println("Students and their enrolled courses (HQL Join):");
                    if (joinResults.isEmpty()) {
                        System.out.println("  No records found.");
                    } else {
                        for (Object[] row : joinResults) {
                            Student s = (Student) row[0];
                            String courseTitle = (String) row[1];
                            System.out.println("  - Student: " + s.getName() + " | Course: " + (courseTitle != null ? courseTitle : "No courses enrolled"));
                        }
                    }
                }
                case 4 -> {
                    int credit = readInt("Enter credit threshold: ");
                    List<Course> courses = courseService.getCoursesWithCreditGreaterThan(credit);
                    System.out.println("Courses with credits > " + credit + " (Criteria API):");
                    if (courses.isEmpty()) {
                        System.out.println("  None found.");
                    } else {
                        for (Course c : courses) {
                            System.out.println("  - [ID: " + c.getId() + "] " + c.getTitle() + " (Credits: " + c.getCredit() + ")");
                        }
                    }
                }
                case 5 -> {
                    List<Object[]> counts = courseService.getStudentsCountPerCourse();
                    System.out.println("Student count per course (Aggregation Query):");
                    if (counts.isEmpty()) {
                        System.out.println("  No courses found.");
                    } else {
                        for (Object[] row : counts) {
                            Course c = (Course) row[0];
                            Long count = (Long) row[1];
                            System.out.println("  - Course: " + c.getTitle() + " | Students enrolled: " + count);
                        }
                    }
                }
                case 6 -> {
                    int courseId = readInt("Enter Course ID: ");
                    Course testCourse = courseService.getCourseById(courseId);
                    if (testCourse == null) {
                        System.out.println("Course not found!");
                        break;
                    }
                    List<Student> courseStudents = studentService.getStudentsEnrolledInCourse(courseId);
                    System.out.println("Students enrolled in course '" + testCourse.getTitle() + "':");
                    if (courseStudents.isEmpty()) {
                        System.out.println("  No students enrolled.");
                    } else {
                        for (Student s : courseStudents) {
                            System.out.println("  - [ID: " + s.getId() + "] " + s.getName() + " (Age: " + s.getAge() + ")");
                        }
                    }
                }
                case 7 -> back = true;
                default -> System.out.println("Invalid option.");
            }
        }
    }

    // --- TIỆN ÍCH NHẬP LIỆU ---
    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String line = scanner.nextLine().trim();
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid number! Please try again.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }
}
