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
import java.util.stream.Collectors;
public class ConsoleApp {
    private final Scanner scanner = new Scanner(System.in);
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    public static void main(String[] args) {
        HibernateUtil.getSessionFactory();
        ConsoleApp app = new ConsoleApp();
        app.seedSampleDataIfEmpty();
        app.run();
        HibernateUtil.shutdown();
    }
    private void run() {
        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    handleStudentMenu();
                    break;
                case 2:
                    handleCourseMenu();
                    break;
                case 3:
                    handleEnrollmentMenu();
                    break;
                case 4:
                    handleReportsMenu();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting application...");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void handleStudentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("Student Management");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    createStudent();
                    break;
                case 2:
                    updateStudent();
                    break;
                case 3:
                    deleteStudent();
                    break;
                case 4:
                    viewStudentById();
                    break;
                case 5:
                    listAllStudents();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void handleCourseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("Course Management");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    createCourse();
                    break;
                case 2:
                    updateCourse();
                    break;
                case 3:
                    deleteCourse();
                    break;
                case 4:
                    viewCourseById();
                    break;
                case 5:
                    listAllCourses();
                    break;
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void handleEnrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("Enrollment Management");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    enrollStudentInCourse();
                    break;
                case 2:
                    removeStudentFromCourse();
                    break;
                case 3:
                    viewCoursesOfStudent();
                    break;
                case 4:
                    viewStudentsOfCourse();
                    break;
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void handleReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println();
            System.out.println("Queries and Reports");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");
            int choice = readInt("Select an option: ");
            switch (choice) {
                case 1:
                    reportStudentsOlderThan();
                    break;
                case 2:
                    reportStudentsByName();
                    break;
                case 3:
                    reportStudentsAndCourses();
                    break;
                case 4:
                    reportCoursesByCredit();
                    break;
                case 5:
                    reportStudentCountsPerCourse();
                    break;
                case 6:
                    reportStudentsByCourse();
                    break;
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
    private void createStudent() {
        String name = readNonEmptyString("Enter student name: ");
        int age = readInt("Enter student age: ");
        Student student = studentService.createStudent(name, age);
        System.out.println("Created: " + student);
    }
    private void updateStudent() {
        int id = readInt("Enter student ID: ");
        String name = readNonEmptyString("Enter new student name: ");
        int age = readInt("Enter new student age: ");
        Student updated = studentService.updateStudent(id, name, age);
        if (updated == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Updated: " + updated);
        }
    }
    private void deleteStudent() {
        int id = readInt("Enter student ID: ");
        boolean deleted = studentService.deleteStudent(id);
        System.out.println(deleted ? "Student deleted." : "Student not found.");
    }
    private void viewStudentById() {
        int id = readInt("Enter student ID: ");
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
        }
    }
    private void listAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(System.out::println);
    }
    private void createCourse() {
        String title = readNonEmptyString("Enter course title: ");
        int credit = readInt("Enter course credit: ");
        Course course = courseService.createCourse(title, credit);
        System.out.println("Created: " + course);
    }
    private void updateCourse() {
        int id = readInt("Enter course ID: ");
        String title = readNonEmptyString("Enter new course title: ");
        int credit = readInt("Enter new course credit: ");
        Course updated = courseService.updateCourse(id, title, credit);
        if (updated == null) {
            System.out.println("Course not found.");
        } else {
            System.out.println("Updated: " + updated);
        }
    }
    private void deleteCourse() {
        int id = readInt("Enter course ID: ");
        boolean deleted = courseService.deleteCourse(id);
        System.out.println(deleted ? "Course deleted." : "Course not found.");
    }
    private void viewCourseById() {
        int id = readInt("Enter course ID: ");
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Course not found.");
        } else {
            System.out.println(course);
        }
    }
    private void listAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        courses.forEach(System.out::println);
    }
    private void enrollStudentInCourse() {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        boolean success = studentService.enrollStudentInCourse(studentId, courseId);
        System.out.println(success ? "Enrollment completed." : "Enrollment failed. Check IDs or existing enrollment.");
    }
    private void removeStudentFromCourse() {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        boolean success = studentService.removeStudentFromCourse(studentId, courseId);
        System.out.println(success ? "Enrollment removed." : "Removal failed. Check IDs or enrollment.");
    }
    private void viewCoursesOfStudent() {
        int studentId = readInt("Enter student ID: ");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        List<Course> courses = studentService.getCoursesOfStudent(studentId);
        System.out.println("Student: " + student.getName());
        if (courses.isEmpty()) {
            System.out.println("No courses enrolled.");
        } else {
            courses.forEach(course -> System.out.println(" - " + course));
        }
    }
    private void viewStudentsOfCourse() {
        int courseId = readInt("Enter course ID: ");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        List<Student> students = courseService.getStudentsOfCourse(courseId);
        System.out.println("Course: " + course.getTitle());
        if (students.isEmpty()) {
            System.out.println("No students enrolled.");
        } else {
            students.forEach(student -> System.out.println(" - " + student));
        }
    }
    private void reportStudentsOlderThan() {
        int age = readInt("Enter minimum age: ");
        List<Student> students = studentService.findStudentsOlderThan(age);
        printStudents(students);
    }
    private void reportStudentsByName() {
        String name = readNonEmptyString("Enter student name to search: ");
        List<Student> students = studentService.findStudentsByName(name);
        printStudents(students);
    }
    private void reportStudentsAndCourses() {
        List<Object[]> rows = studentService.listStudentsWithCourses();
        if (rows.isEmpty()) {
            System.out.println("No enrollments found.");
            return;
        }
        for (Object[] row : rows) {
            Student student = (Student) row[0];
            String title = (String) row[1];
            System.out.println(student.getName() + " -> " + title);
        }
    }
    private void reportCoursesByCredit() {
        int credit = readInt("Enter minimum credit: ");
        List<Course> courses = courseService.findCoursesWithCreditGreaterThan(credit);
        printCourses(courses);
    }
    private void reportStudentCountsPerCourse() {
        List<Object[]> rows = courseService.countStudentsInEachCourse();
        if (rows.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        for (Object[] row : rows) {
            Course course = (Course) row[0];
            Long count = (Long) row[1];
            System.out.println("Course: " + course.getTitle() + " | Students enrolled: " + count);
        }
    }
    private void reportStudentsByCourse() {
        int courseId = readInt("Enter course ID: ");
        List<Student> students = studentService.findStudentsEnrolledInCourse(courseId);
        printStudents(students);
    }
    private void printStudents(List<Student> students) {
        if (students == null || students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        students.forEach(System.out::println);
    }
    private void printCourses(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        courses.forEach(System.out::println);
    }
    private void seedSampleDataIfEmpty() {
        if (!studentService.getAllStudents().isEmpty() || !courseService.getAllCourses().isEmpty()) {
            return;
        }
        Course java = courseService.createCourse("Java Programming", 4);
        Course db = courseService.createCourse("Database Systems", 3);
        Course web = courseService.createCourse("Web Development", 4);
        Student alice = studentService.createStudent("Alice", 22);
        Student bob = studentService.createStudent("Bob", 24);
        Student carol = studentService.createStudent("Carol", 21);
        studentService.enrollStudentInCourse(alice.getId(), java.getId());
        studentService.enrollStudentInCourse(alice.getId(), db.getId());
        studentService.enrollStudentInCourse(bob.getId(), java.getId());
        studentService.enrollStudentInCourse(carol.getId(), web.getId());
    }
    private void printMainMenu() {
        System.out.println();
        System.out.println("Main Menu");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Queries and Reports");
        System.out.println("5. Exit");
    }
    private int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException ex) {
                System.out.println("Please enter a valid integer.");
            }
        }
    }
    private String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = scanner.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("Input cannot be empty.");
        }
    }
}
