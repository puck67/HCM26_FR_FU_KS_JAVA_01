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

public class App {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        try {
            HibernateUtil.getSessionFactory();
        } catch (Exception e) {
            System.err.println("Database initialization failed. Exiting application.");
            e.printStackTrace();
            System.exit(1);
        }

        while (running) {
            printMainMenu();
            int choice = readMenuChoice(scanner, "Select an option: ");
            switch (choice) {
                case 1 -> studentMenu(scanner);
                case 2 -> courseMenu(scanner);
                case 3 -> enrollmentMenu(scanner);
                case 4 -> queriesMenu(scanner);
                case 5 -> {
                    System.out.println("Exiting the application. Goodbye!");
                    HibernateUtil.shutdown();
                    running = false;
                }
                default -> System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
        scanner.close();
    }

    private static void printMainMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n================ MAIN MENU ================\n")
          .append("1. Student Management\n")
          .append("2. Course Management\n")
          .append("3. Enrollment Management\n")
          .append("4. Queries and Reports\n")
          .append("5. Exit\n")
          .append("===========================================\n");
        System.out.print(sb.toString());
    }

    private static void studentMenu(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            printStudentSubMenu();
            int choice = readMenuChoice(scanner, "Select an option (1-6): ");
            switch (choice) {
                case 1 -> addStudent(scanner);
                case 2 -> updateStudentMenu(scanner);
                case 3 -> deleteStudentMenu(scanner);
                case 4 -> displayStudentById(scanner);
                case 5 -> displayAllStudents();
                case 6 -> inSubMenu = false;
                default -> System.out.println("Invalid option. Please choose between 1 and 6.");
            }
        }
    }

    private static void printStudentSubMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n============ STUDENT MANAGEMENT ============\n")
          .append("1. Create a new student\n")
          .append("2. Update student information\n")
          .append("3. Delete a student\n")
          .append("4. View student by ID\n")
          .append("5. List all students\n")
          .append("6. Back to main menu\n")
          .append("============================================\n");
        System.out.print(sb.toString());
    }

    private static void courseMenu(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            printCourseSubMenu();
            int choice = readMenuChoice(scanner, "Select an option (1-6): ");
            switch (choice) {
                case 1 -> addCourse(scanner);
                case 2 -> updateCourseMenu(scanner);
                case 3 -> deleteCourseMenu(scanner);
                case 4 -> displayCourseById(scanner);
                case 5 -> displayAllCourses();
                case 6 -> inSubMenu = false;
                default -> System.out.println("Invalid option. Please choose between 1 and 6.");
            }
        }
    }

    private static void printCourseSubMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n============ COURSE MANAGEMENT ============\n")
          .append("1. Create a new course\n")
          .append("2. Update course information\n")
          .append("3. Delete a course\n")
          .append("4. View course by ID\n")
          .append("5. List all courses\n")
          .append("6. Back to main menu\n")
          .append("===========================================\n");
        System.out.print(sb.toString());
    }

    private static void enrollmentMenu(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            printEnrollmentSubMenu();
            int choice = readMenuChoice(scanner, "Select an option (1-5): ");
            switch (choice) {
                case 1 -> enrollStudentMenu(scanner);
                case 2 -> removeStudentFromCourseMenu(scanner);
                case 3 -> displayCoursesOfStudent(scanner);
                case 4 -> displayStudentsOfCourse(scanner);
                case 5 -> inSubMenu = false;
                default -> System.out.println("Invalid option. Please choose between 1 and 5.");
            }
        }
    }

    private static void printEnrollmentSubMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n========== ENROLLMENT MANAGEMENT ==========\n")
          .append("1. Enroll a student in a course\n")
          .append("2. Remove a student from a course\n")
          .append("3. View courses of a student\n")
          .append("4. View students of a course\n")
          .append("5. Back to main menu\n")
          .append("===========================================\n");
        System.out.print(sb.toString());
    }

    private static void queriesMenu(Scanner scanner) {
        boolean inSubMenu = true;
        while (inSubMenu) {
            printQueriesSubMenu();
            int choice = readMenuChoice(scanner, "Select an option (1-7): ");
            switch (choice) {
                case 1 -> displayStudentsOlderThan(scanner);
                case 2 -> displayStudentsByName(scanner);
                case 3 -> displayStudentsAndCourses();
                case 4 -> displayCoursesByCredit(scanner);
                case 5 -> displayCourseEnrollmentCounts();
                case 6 -> displayStudentsInCourse(scanner);
                case 7 -> inSubMenu = false;
                default -> System.out.println("Invalid option. Please choose between 1 and 7.");
            }
        }
    }

    private static void printQueriesSubMenu() {
        StringBuilder sb = new StringBuilder();
        sb.append("\n============== QUERIES AND REPORTS ==============\n")
          .append("1. Find students older than a given age (HQL)\n")
          .append("2. Find students by name (Named Query)\n")
          .append("3. List students and their courses (HQL Join)\n")
          .append("4. Find courses with credit greater than a value (Criteria API)\n")
          .append("5. Count number of students in each course (Aggregation Query)\n")
          .append("6. Find students enrolled in a specific course\n")
          .append("7. Back to main menu\n")
          .append("=================================================\n");
        System.out.print(sb.toString());
    }

    private static int readMenuChoice(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid integer choice.");
            }
        }
    }

    private static int readInt(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            try {
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Error: Input must be a valid integer. Please try again.");
            }
        }
    }

    private static String readStudentName(Scanner scanner, String prompt) {
        String regex = "^[\\p{L}\\s']+$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Student name cannot be empty. Please try again.");
                continue;
            }
            if (input.matches(regex)) {
                return input;
            }
            System.out.println("Error: Student name is invalid (numbers or special characters not allowed). Please try again.");
        }
    }

    private static int readAge(Scanner scanner, String prompt) {
        while (true) {
            int age = readInt(scanner, prompt);
            if (age >= 6 && age <= 100) {
                return age;
            }
            System.out.println("Error: Student age must be between 6 and 100. Please try again.");
        }
    }

    private static String readCourseTitle(Scanner scanner, String prompt) {
        String regex = "^[\\p{L}0-9\\s\\-\\+\\#\\.]+$";
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("Error: Course title cannot be empty. Please try again.");
                continue;
            }
            if (input.length() < 2 || input.length() > 100) {
                System.out.println("Error: Course title must be between 2 and 100 characters. Please try again.");
                continue;
            }
            if (input.matches(regex)) {
                return input;
            }
            System.out.println("Error: Course title contains invalid characters. Please try again.");
        }
    }

    private static int readCourseCredit(Scanner scanner, String prompt) {
        while (true) {
            int credit = readInt(scanner, prompt);
            if (credit >= 1 && credit <= 10) {
                return credit;
            }
            System.out.println("Error: Course credits must be between 1 and 10. Please try again.");
        }
    }

    private static int readExistingStudentId(Scanner scanner, String prompt) {
        while (true) {
            int id = readInt(scanner, prompt);
            if (id == -1) {
                return -1;
            }
            try {
                studentService.getStudentById(id);
                return id;
            } catch (Exception e) {
                System.out.println("Error: Student with ID " + id + " not found. Please enter another ID (or -1 to cancel).");
            }
        }
    }

    private static int readExistingCourseId(Scanner scanner, String prompt) {
        while (true) {
            int id = readInt(scanner, prompt);
            if (id == -1) {
                return -1;
            }
            try {
                courseService.getCourseById(id);
                return id;
            } catch (Exception e) {
                System.out.println("Error: Course with ID " + id + " not found. Please enter another ID (or -1 to cancel).");
            }
        }
    }

    private static void addStudent(Scanner scanner) {
        System.out.println("\n--- Create New Student ---");
        String name = readStudentName(scanner, "Enter student name: ");
        int age = readAge(scanner, "Enter student age: ");
        try {
            Student student = studentService.createStudent(name, age);
            System.out.println("Success: Student created successfully -> " + student);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateStudentMenu(Scanner scanner) {
        System.out.println("\n--- Update Student Information ---");
        int id = readExistingStudentId(scanner, "Enter student ID to update (or -1 to cancel): ");
        if (id == -1) {
            System.out.println("Update cancelled.");
            return;
        }
        String name = readStudentName(scanner, "Enter new student name: ");
        int age = readAge(scanner, "Enter new student age: ");
        try {
            studentService.updateStudent(id, name, age);
            System.out.println("Success: Student information updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteStudentMenu(Scanner scanner) {
        System.out.println("\n--- Delete Student ---");
        int id = readExistingStudentId(scanner, "Enter student ID to delete (or -1 to cancel): ");
        if (id == -1) {
            System.out.println("Delete cancelled.");
            return;
        }
        try {
            studentService.deleteStudent(id);
            System.out.println("Success: Student deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayStudentById(Scanner scanner) {
        System.out.println("\n--- View Student Details ---");
        int id = readExistingStudentId(scanner, "Enter student ID to view (or -1 to cancel): ");
        if (id == -1) return;
        try {
            Student student = studentService.getStudentById(id);
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== STUDENT DETAILS ===\n")
              .append("ID: ").append(student.getId()).append("\n")
              .append("Name: ").append(student.getName()).append("\n")
              .append("Age: ").append(student.getAge()).append("\n")
              .append("========================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayAllStudents() {
        try {
            List<Student> students = studentService.getAllStudents();
            if (students.isEmpty()) {
                System.out.println("No students found in the system.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n==================== ALL STUDENTS ====================\n")
              .append(String.format("%-10s %-35s %-10s\n", "ID", "Student Name", "Age"))
              .append("------------------------------------------------------------\n");
            for (Student s : students) {
                sb.append(String.format("%-10d %-35s %-10d\n", s.getId(), s.getName(), s.getAge()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void addCourse(Scanner scanner) {
        System.out.println("\n--- Create New Course ---");
        String title = readCourseTitle(scanner, "Enter course title: ");
        int credit = readCourseCredit(scanner, "Enter course credits (1 - 10): ");
        try {
            Course course = courseService.createCourse(title, credit);
            System.out.println("Success: Course created successfully! -> " + course);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateCourseMenu(Scanner scanner) {
        System.out.println("\n--- Update Course Information ---");
        int id = readExistingCourseId(scanner, "Enter course ID to update (or -1 to cancel): ");
        if (id == -1) {
            System.out.println("Update cancelled.");
            return;
        }
        String title = readCourseTitle(scanner, "Enter new course title: ");
        int credit = readCourseCredit(scanner, "Enter new course credits: ");
        try {
            courseService.updateCourse(id, title, credit);
            System.out.println("Success: Course information updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteCourseMenu(Scanner scanner) {
        System.out.println("\n--- Delete Course ---");
        int id = readExistingCourseId(scanner, "Enter course ID to delete (or -1 to cancel): ");
        if (id == -1) {
            System.out.println("Delete cancelled.");
            return;
        }
        try {
            courseService.deleteCourse(id);
            System.out.println("Success: Course deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayCourseById(Scanner scanner) {
        System.out.println("\n--- View Course Details ---");
        int id = readExistingCourseId(scanner, "Enter course ID to view (or -1 to cancel): ");
        if (id == -1) return;
        try {
            Course course = courseService.getCourseById(id);
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== COURSE DETAILS ===\n")
              .append("ID: ").append(course.getId()).append("\n")
              .append("Title: ").append(course.getTitle()).append("\n")
              .append("Credits: ").append(course.getCredit()).append("\n")
              .append("======================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayAllCourses() {
        try {
            List<Course> courses = courseService.getAllCourses();
            if (courses.isEmpty()) {
                System.out.println("No courses found in the system.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n==================== ALL COURSES ====================\n")
              .append(String.format("%-10s %-40s %-10s\n", "ID", "Course Title", "Credits"))
              .append("------------------------------------------------------------\n");
            for (Course c : courses) {
                sb.append(String.format("%-10d %-40s %-10d\n", c.getId(), c.getTitle(), c.getCredit()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void enrollStudentMenu(Scanner scanner) {
        System.out.println("\n--- Enroll Student in Course ---");
        int studentId = readExistingStudentId(scanner, "Enter student ID (or -1 to cancel): ");
        if (studentId == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        while (true) {
            int courseId = readExistingCourseId(scanner, "Enter course ID to enroll (or -1 to cancel): ");
            if (courseId == -1) {
                System.out.println("Enrollment cancelled.");
                return;
            }
            try {
                studentService.enrollStudentInCourse(studentId, courseId);
                System.out.println("Success: Student (ID: " + studentId + ") enrolled in course (ID: " + courseId + ") successfully!");
                break;
            } catch (Exception e) {
                System.out.println("Enrollment error: " + e.getMessage() + ". Please enter another course ID.");
            }
        }
    }

    private static void removeStudentFromCourseMenu(Scanner scanner) {
        System.out.println("\n--- Remove Student from Course ---");
        int studentId = readExistingStudentId(scanner, "Enter student ID (or -1 to cancel): ");
        if (studentId == -1) {
            System.out.println("Operation cancelled.");
            return;
        }

        while (true) {
            int courseId = readExistingCourseId(scanner, "Enter course ID to remove (or -1 to cancel): ");
            if (courseId == -1) {
                System.out.println("Operation cancelled.");
                return;
            }
            try {
                studentService.removeStudentFromCourse(studentId, courseId);
                System.out.println("Success: Student (ID: " + studentId + ") removed from course (ID: " + courseId + ") successfully!");
                break;
            } catch (Exception e) {
                System.out.println("Removal error: " + e.getMessage() + ". Please enter another course ID.");
            }
        }
    }

    private static void displayCoursesOfStudent(Scanner scanner) {
        System.out.println("\n--- View Courses of Student ---");
        int studentId = readExistingStudentId(scanner, "Enter student ID (or -1 to cancel): ");
        if (studentId == -1) return;
        try {
            Student student = studentService.getStudentById(studentId);
            Set<Course> courses = studentService.getCoursesOfStudent(studentId);
            if (courses.isEmpty()) {
                System.out.println("Student '" + student.getName() + "' (ID: " + studentId + ") is not enrolled in any courses.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== COURSES OF STUDENT '").append(student.getName()).append("' (ID: ").append(studentId).append(") ===\n")
              .append(String.format("%-10s %-40s %-10s\n", "ID", "Course Title", "Credits"))
              .append("----------------------------------------------------------------\n");
            for (Course c : courses) {
                sb.append(String.format("%-10d %-40s %-10d\n", c.getId(), c.getTitle(), c.getCredit()));
            }
            sb.append("================================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayStudentsOfCourse(Scanner scanner) {
        System.out.println("\n--- View Enrolled Students ---");
        int courseId = readExistingCourseId(scanner, "Enter course ID to view students (or -1 to cancel): ");
        if (courseId == -1) return;
        try {
            Course course = courseService.getCourseById(courseId);
            Set<Student> students = courseService.getStudentsOfCourse(courseId);
            if (students.isEmpty()) {
                System.out.println("Course '" + course.getTitle() + "' (ID: " + courseId + ") has no students enrolled.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n=== STUDENTS IN COURSE '").append(course.getTitle()).append("' (ID: ").append(courseId).append(") ===\n")
              .append(String.format("%-10s %-35s %-10s\n", "ID", "Student Name", "Age"))
              .append("------------------------------------------------------------\n");
            for (Student s : students) {
                sb.append(String.format("%-10d %-35s %-10d\n", s.getId(), s.getName(), s.getAge()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayStudentsOlderThan(Scanner scanner) {
        System.out.println("\n--- Find Students Older Than Age (HQL) ---");
        int age;
        while (true) {
            age = readInt(scanner, "Enter age threshold (between 0 and 100): ");
            if (age >= 0 && age <= 100) {
                break;
            }
            System.out.println("Error: Valid age must be between 0 and 100. Please try again.");
        }

        try {
            List<Student> students = studentService.findStudentsOlderThan(age);
            if (students.isEmpty()) {
                System.out.println("No students found older than " + age + ".");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n================ STUDENTS OLDER THAN ").append(age).append(" (HQL) ================\n")
              .append(String.format("%-10s %-35s %-10s\n", "ID", "Student Name", "Age"))
              .append("------------------------------------------------------------\n");
            for (Student s : students) {
                sb.append(String.format("%-10d %-35s %-10d\n", s.getId(), s.getName(), s.getAge()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayStudentsByName(Scanner scanner) {
        System.out.println("\n--- Find Students By Name (Named Query) ---");
        String name = readStudentName(scanner, "Enter student name to find: ");
        try {
            List<Student> students = studentService.findStudentsByName(name);
            if (students.isEmpty()) {
                System.out.println("No students found with name '" + name + "'.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n================ STUDENTS NAMED '").append(name).append("' (Named Query) ================\n")
              .append(String.format("%-10s %-35s %-10s\n", "ID", "Student Name", "Age"))
              .append("------------------------------------------------------------\n");
            for (Student s : students) {
                sb.append(String.format("%-10d %-35s %-10d\n", s.getId(), s.getName(), s.getAge()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayStudentsAndCourses() {
        try {
            List<Student> students = studentService.getStudentsWithCourses();
            if (students.isEmpty()) {
                System.out.println("No student data found in the system.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n==================== REPORT: STUDENTS & ENROLLED COURSES (HQL Join) ====================\n")
              .append(String.format("%-6s %-25s %-5s | %-45s\n", "ID", "Student Name", "Age", "Enrolled Courses"))
              .append("--------------------------------------------------------------------------------------------\n");
            for (Student s : students) {
                StringBuilder coursesSb = new StringBuilder();
                if (s.getCourses().isEmpty()) {
                    coursesSb.append("[No Courses Enrolled]");
                } else {
                    coursesSb.append("[");
                    int count = 0;
                    for (Course c : s.getCourses()) {
                        coursesSb.append(c.getTitle());
                        if (++count < s.getCourses().size()) {
                            coursesSb.append(", ");
                        }
                    }
                    coursesSb.append("]");
                }
                sb.append(String.format("%-6d %-25s %-5d | %-45s\n", s.getId(), s.getName(), s.getAge(), coursesSb.toString()));
            }
            sb.append("============================================================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayCoursesByCredit(Scanner scanner) {
        System.out.println("\n--- Find Courses By Credits (Criteria API) ---");
        int credit = readCourseCredit(scanner, "Enter credit threshold: ");
        try {
            List<Course> courses = courseService.findCoursesWithCreditGreaterThan(credit);
            if (courses.isEmpty()) {
                System.out.println("No courses found with credits greater than " + credit + ".");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n================ COURSES WITH CREDITS > ").append(credit).append(" (Criteria API) ================\n")
              .append(String.format("%-10s %-40s %-10s\n", "ID", "Course Title", "Credits"))
              .append("------------------------------------------------------------\n");
            for (Course c : courses) {
                sb.append(String.format("%-10d %-40s %-10d\n", c.getId(), c.getTitle(), c.getCredit()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayCourseEnrollmentCounts() {
        try {
            List<Object[]> counts = courseService.getCourseEnrollmentCounts();
            if (counts.isEmpty()) {
                System.out.println("No courses found in the system.");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n================ ENROLLMENT STATS BY COURSE (Aggregation) ================\n");
            for (Object[] row : counts) {
                String courseTitle = (String) row[0];
                Long studentCount = (Long) row[1];
                sb.append("Course: ").append(String.format("%-35s", courseTitle))
                  .append(" | Students enrolled: ").append(studentCount)
                  .append("\n");
            }
            sb.append("==========================================================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void displayStudentsInCourse(Scanner scanner) {
        System.out.println("\n--- View Enrolled Students in Course ---");
        int courseId = readExistingCourseId(scanner, "Enter course ID to query (or -1 to cancel): ");
        if (courseId == -1) return;
        try {
            Course course = courseService.getCourseById(courseId);
            List<Student> students = studentService.getStudentsEnrolledInCourse(courseId);
            if (students.isEmpty()) {
                System.out.println("No students enrolled in course '" + course.getTitle() + "' (ID: " + courseId + ").");
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("\n================ STUDENTS IN COURSE '").append(course.getTitle()).append("' (ID: ").append(courseId).append(") ================\n")
              .append(String.format("%-10s %-35s %-10s\n", "ID", "Student Name", "Age"))
              .append("------------------------------------------------------------\n");
            for (Student s : students) {
                sb.append(String.format("%-10d %-35s %-10d\n", s.getId(), s.getName(), s.getAge()));
            }
            sb.append("============================================================\n");
            System.out.print(sb.toString());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
