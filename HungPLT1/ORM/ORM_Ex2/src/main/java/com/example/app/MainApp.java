package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class MainApp {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        try {
            // Eager initialize Hibernate SessionFactory
            HibernateUtil.getSessionFactory();
            
            boolean exit = false;
            while (!exit) {
                System.out.println("\n=== MAIN MENU ===");
                System.out.println("1. Student Management");
                System.out.println("2. Course Management");
                System.out.println("3. Enrollment Management");
                System.out.println("4. Queries and Reports");
                System.out.println("5. Exit");
                System.out.print("Select an option (1-5): ");
                
                String choice = scanner.nextLine().trim();
                switch (choice) {
                    case "1" -> handleStudentMenu();
                    case "2" -> handleCourseMenu();
                    case "3" -> handleEnrollmentMenu();
                    case "4" -> handleQueriesMenu();
                    case "5" -> {
                        exit = true;
                        System.out.println("Exiting the application... Goodbye!");
                    }
                    default -> System.out.println("Invalid option. Please choose a number between 1 and 5.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    // --- Student Actions ---

    private static void handleStudentMenu() {
        while (true) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            System.out.print("Select an option (1-6): ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createStudentAction();
                case "2" -> updateStudentAction();
                case "3" -> deleteStudentAction();
                case "4" -> viewStudentAction();
                case "5" -> listAllStudentsAction();
                case "6" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createStudentAction() {
        String name = readString("Enter student name: ");
        int age = readPositiveInt("Enter student age: ");
        studentService.createStudent(name, age);
        System.out.println("Student created successfully.");
    }

    private static void updateStudentAction() {
        int id = readPositiveInt("Enter student ID to update: ");
        Student student = studentService.getStudentById(id);
        if (student == null) {
            System.out.println("Student with ID " + id + " not found.");
            return;
        }
        String newName = readString("Enter new student name: ");
        int newAge = readPositiveInt("Enter new student age: ");
        studentService.updateStudent(id, newName, newAge);
        System.out.println("Student updated successfully.");
    }

    private static void deleteStudentAction() {
        int deleteId = readPositiveInt("Enter student ID to delete: ");
        Student student = studentService.getStudentById(deleteId);
        if (student == null) {
            System.out.println("Student with ID " + deleteId + " not found.");
            return;
        }
        studentService.deleteStudent(deleteId);
        System.out.println("Student deleted successfully.");
    }

    private static void viewStudentAction() {
        int viewId = readPositiveInt("Enter student ID to view: ");
        Student student = studentService.getStudentById(viewId);
        if (student != null) {
            System.out.println("\n[Student Details]");
            System.out.println("ID: " + student.getId());
            System.out.println("Name: " + student.getName());
            System.out.println("Age: " + student.getAge());
            System.out.println("Enrolled Courses: ");
            student.getCourses().forEach(c -> System.out.println(" - " + c.getTitle() + " (Credits: " + c.getCredit() + ")"));
        } else {
            System.out.println("Student not found with ID: " + viewId);
        }
    }

    private static void listAllStudentsAction() {
        List<Student> students = studentService.getAllStudents();
        System.out.println("\n[All Students]");
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            students.forEach(s -> System.out.println("ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge() + " | Enrolled in " + s.getCourses().size() + " course(s)"));
        }
    }

    // --- Course Actions ---

    private static void handleCourseMenu() {
        while (true) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            System.out.print("Select an option (1-6): ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> createCourseAction();
                case "2" -> updateCourseAction();
                case "3" -> deleteCourseAction();
                case "4" -> viewCourseAction();
                case "5" -> listAllCoursesAction();
                case "6" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void createCourseAction() {
        String title = readString("Enter course title: ");
        int credit = readPositiveInt("Enter course credits: ");
        courseService.createCourse(title, credit);
        System.out.println("Course created successfully.");
    }

    private static void updateCourseAction() {
        int id = readPositiveInt("Enter course ID to update: ");
        Course course = courseService.getCourseById(id);
        if (course == null) {
            System.out.println("Course with ID " + id + " not found.");
            return;
        }
        String newTitle = readString("Enter new course title: ");
        int newCredit = readPositiveInt("Enter new course credits: ");
        courseService.updateCourse(id, newTitle, newCredit);
        System.out.println("Course updated successfully.");
    }

    private static void deleteCourseAction() {
        int deleteId = readPositiveInt("Enter course ID to delete: ");
        Course course = courseService.getCourseById(deleteId);
        if (course == null) {
            System.out.println("Course with ID " + deleteId + " not found.");
            return;
        }
        courseService.deleteCourse(deleteId);
        System.out.println("Course deleted successfully.");
    }

    private static void viewCourseAction() {
        int viewId = readPositiveInt("Enter course ID to view: ");
        Course course = courseService.getCourseById(viewId);
        if (course != null) {
            System.out.println("\n[Course Details]");
            System.out.println("ID: " + course.getId());
            System.out.println("Title: " + course.getTitle());
            System.out.println("Credit: " + course.getCredit());
            System.out.println("Enrolled Students: ");
            course.getStudents().forEach(s -> System.out.println(" - " + s.getName() + " (Age: " + s.getAge() + ")"));
        } else {
            System.out.println("Course not found with ID: " + viewId);
        }
    }

    private static void listAllCoursesAction() {
        List<Course> courses = courseService.getAllCourses();
        System.out.println("\n[All Courses]");
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            courses.forEach(c -> System.out.println("ID: " + c.getId() + " | Title: " + c.getTitle() + " | Credit: " + c.getCredit() + " | Enrolled: " + c.getStudents().size() + " student(s)"));
        }
    }

    // --- Enrollment Actions ---

    private static void handleEnrollmentMenu() {
        while (true) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll a student in a course");
            System.out.println("2. Remove a student from a course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            System.out.print("Select an option (1-5): ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> enrollStudentAction();
                case "2" -> removeStudentAction();
                case "3" -> viewCoursesOfStudentAction();
                case "4" -> viewStudentsOfCourseAction();
                case "5" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void enrollStudentAction() {
        int studentId = readPositiveInt("Enter Student ID: ");
        Student student = studentService.getStudentById(studentId);
        if (student == null) {
            System.out.println("Student with ID " + studentId + " not found.");
            return;
        }
        int courseId = readPositiveInt("Enter Course ID: ");
        Course course = courseService.getCourseById(courseId);
        if (course == null) {
            System.out.println("Course with ID " + courseId + " not found.");
            return;
        }
        studentService.enrollStudentInCourse(studentId, courseId);
        System.out.println("Enrollment request processed successfully.");
    }

    private static void removeStudentAction() {
        int sId = readPositiveInt("Enter Student ID: ");
        Student student = studentService.getStudentById(sId);
        if (student == null) {
            System.out.println("Student with ID " + sId + " not found.");
            return;
        }
        int cId = readPositiveInt("Enter Course ID: ");
        Course course = courseService.getCourseById(cId);
        if (course == null) {
            System.out.println("Course with ID " + cId + " not found.");
            return;
        }
        studentService.removeStudentFromCourse(sId, cId);
        System.out.println("Removal request processed successfully.");
    }

    private static void viewCoursesOfStudentAction() {
        int viewSId = readPositiveInt("Enter Student ID: ");
        Set<Course> courses = studentService.getCoursesOfStudent(viewSId);
        System.out.println("\n[Courses for Student ID " + viewSId + "]");
        if (courses.isEmpty()) {
            System.out.println("No courses enrolled or student not found.");
        } else {
            courses.forEach(c -> System.out.println(" - ID: " + c.getId() + " | Title: " + c.getTitle() + " | Credit: " + c.getCredit()));
        }
    }

    private static void viewStudentsOfCourseAction() {
        int viewCId = readPositiveInt("Enter Course ID: ");
        Set<Student> students = courseService.getStudentsOfCourse(viewCId);
        System.out.println("\n[Students enrolled in Course ID " + viewCId + "]");
        if (students.isEmpty()) {
            System.out.println("No students enrolled or course not found.");
        } else {
            students.forEach(s -> System.out.println(" - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    // --- Query Actions ---

    private static void handleQueriesMenu() {
        while (true) {
            System.out.println("\n--- Queries and Reports ---");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course");
            System.out.println("7. Back to main menu");
            System.out.print("Select an option (1-7): ");
            
            String choice = scanner.nextLine().trim();
            switch (choice) {
                case "1" -> queryStudentsOlderThanAction();
                case "2" -> queryStudentsByNameAction();
                case "3" -> queryListStudentsAndCoursesAction();
                case "4" -> queryCoursesWithCreditLimitAction();
                case "5" -> queryCountStudentsPerCourseAction();
                case "6" -> queryStudentsEnrolledInCourseAction();
                case "7" -> {
                    return;
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void queryStudentsOlderThanAction() {
        int age = readPositiveInt("Enter age: ");
        List<Student> studentsOlder = studentService.getStudentsOlderThan(age);
        System.out.println("\n[Students older than " + age + "]");
        if (studentsOlder.isEmpty()) {
            System.out.println("No students match criteria.");
        } else {
            studentsOlder.forEach(s -> System.out.println(" - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    private static void queryStudentsByNameAction() {
        String name = readString("Enter name: ");
        List<Student> studentsByName = studentService.getStudentsByName(name);
        System.out.println("\n[Students named '" + name + "']");
        if (studentsByName.isEmpty()) {
            System.out.println("No students found with name: " + name);
        } else {
            studentsByName.forEach(s -> System.out.println(" - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    private static void queryListStudentsAndCoursesAction() {
        List<Object[]> list = studentService.getAllStudentsWithCourses();
        System.out.println("\n[Students and their Courses]");
        if (list.isEmpty()) {
            System.out.println("No records found.");
        } else {
            list.forEach(row -> {
                Student s = (Student) row[0];
                String courseTitle = (String) row[1];
                System.out.println(" - Student: " + s.getName() + " (Age: " + s.getAge() + ") | Course: " + (courseTitle != null ? courseTitle : "No courses"));
            });
        }
    }

    private static void queryCoursesWithCreditLimitAction() {
        int creditLimit = readPositiveOrZeroInt("Enter minimum credit limit: ");
        List<Course> coursesCredits = courseService.getCoursesWithCreditGreaterThan(creditLimit);
        System.out.println("\n[Courses with credits > " + creditLimit + "]");
        if (coursesCredits.isEmpty()) {
            System.out.println("No courses match criteria.");
        } else {
            coursesCredits.forEach(c -> System.out.println(" - ID: " + c.getId() + " | Title: " + c.getTitle() + " | Credit: " + c.getCredit()));
        }
    }

    private static void queryCountStudentsPerCourseAction() {
        Map<String, Long> countMap = courseService.getStudentCountPerCourse();
        System.out.println("\n[Enrollment Counts per Course]");
        if (countMap.isEmpty()) {
            System.out.println("No courses/enrollments found.");
        } else {
            countMap.forEach((title, count) -> System.out.println(" - Course: " + title + " | Students enrolled: " + count));
        }
    }

    private static void queryStudentsEnrolledInCourseAction() {
        int courseId = readPositiveInt("Enter Course ID: ");
        List<Student> enrolledStudents = studentService.getStudentsEnrolledInCourse(courseId);
        System.out.println("\n[Students enrolled in Course ID " + courseId + "]");
        if (enrolledStudents.isEmpty()) {
            System.out.println("No students found in this course.");
        } else {
            enrolledStudents.forEach(s -> System.out.println(" - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    // --- Input Validation Helpers ---

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value > 0) {
                    return value;
                }
                System.out.println("Value must be a positive integer greater than 0.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }

    private static int readPositiveOrZeroInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(scanner.nextLine().trim());
                if (value >= 0) {
                    return value;
                }
                System.out.println("Value must be 0 or a positive integer.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
