import dao.CourseDAO;
import dao.StudentDAO;
import entity.Course;
import entity.Student;
import util.HibernateUtil;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * Main class - Console UI for Student & Course Management System.
 */
public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO  courseDAO  = new CourseDAO();

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1": studentMenu();    break;
                    case "2": courseMenu();     break;
                    case "3": enrollMenu();     break;
                    case "4": queryMenu();      break;
                    case "5": bonusMenu();      break;
                    case "6":
                        System.out.println("Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    // ===================== MAIN MENU =====================
    private static void printMainMenu() {
        System.out.println("\n========== STUDENT MANAGEMENT SYSTEM ==========");
        System.out.println("1. Student management");
        System.out.println("2. Course management");
        System.out.println("3. Enrollment management");
        System.out.println("4. Queries (Task 5)");
        System.out.println("5. Bonus queries");
        System.out.println("6. Exit");
        System.out.print("Your choice: ");
    }

    // ===================== STUDENT MENU =====================
    private static void studentMenu() {
        System.out.println("\n--- Student Management ---");
        System.out.println("1. Add new student");
        System.out.println("2. Update student");
        System.out.println("3. Delete student");
        System.out.println("4. Get student by ID");
        System.out.println("5. Get all students");
        System.out.print("Your choice: ");
        switch (sc.nextLine().trim()) {
            case "1": addStudent();        break;
            case "2": updateStudent();     break;
            case "3": deleteStudent();     break;
            case "4": getStudentById();    break;
            case "5": getAllStudents();     break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void addStudent() {
        try {
            System.out.print("Name: ");
            String name = sc.nextLine().trim();

            int age = 0;
            while (true) {
                System.out.print("Age: ");
                try {
                    age = Integer.parseInt(sc.nextLine().trim());
                    if (age <= 0) { System.out.println("  Age must be positive."); continue; }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input.");
                }
            }

            studentDAO.create(new Student(name, age));
            System.out.println("Student added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        try {
            int id = inputId("Student ID to update");
            Student existing = studentDAO.getById(id);
            if (existing == null) { System.out.println("Student not found."); return; }
            System.out.println("Current: " + existing);

            System.out.print("New Name: ");
            String name = sc.nextLine().trim();

            int age = 0;
            while (true) {
                System.out.print("New Age: ");
                try {
                    age = Integer.parseInt(sc.nextLine().trim());
                    if (age <= 0) { System.out.println("  Age must be positive."); continue; }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input.");
                }
            }

            existing.setName(name);
            existing.setAge(age);
            studentDAO.update(existing);
            System.out.println("Student updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try {
            int id = inputId("Student ID to delete");
            studentDAO.delete(id);
            System.out.println("Student deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getStudentById() {
        try {
            int id = inputId("Student ID");
            Student s = studentDAO.getById(id);
            if (s == null) System.out.println("Student not found.");
            else System.out.println("Found: " + s);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getAllStudents() {
        List<Student> list = studentDAO.getAll();
        if (list.isEmpty()) System.out.println("No students found.");
        else list.forEach(System.out::println);
    }

    // ===================== COURSE MENU =====================
    private static void courseMenu() {
        System.out.println("\n--- Course Management ---");
        System.out.println("1. Add new course");
        System.out.println("2. Update course");
        System.out.println("3. Delete course");
        System.out.println("4. Get course by ID");
        System.out.println("5. Get all courses");
        System.out.print("Your choice: ");
        switch (sc.nextLine().trim()) {
            case "1": addCourse();      break;
            case "2": updateCourse();   break;
            case "3": deleteCourse();   break;
            case "4": getCourseById();  break;
            case "5": getAllCourses();   break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void addCourse() {
        try {
            System.out.print("Title: ");
            String title = sc.nextLine().trim();

            int credit = 0;
            while (true) {
                System.out.print("Credit: ");
                try {
                    credit = Integer.parseInt(sc.nextLine().trim());
                    if (credit <= 0) { System.out.println("  Credit must be positive."); continue; }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input.");
                }
            }

            courseDAO.create(new Course(title, credit));
            System.out.println("Course added successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateCourse() {
        try {
            int id = inputId("Course ID to update");
            Course existing = courseDAO.getById(id);
            if (existing == null) { System.out.println("Course not found."); return; }
            System.out.println("Current: " + existing);

            System.out.print("New Title: ");
            String title = sc.nextLine().trim();

            int credit = 0;
            while (true) {
                System.out.print("New Credit: ");
                try {
                    credit = Integer.parseInt(sc.nextLine().trim());
                    if (credit <= 0) { System.out.println("  Credit must be positive."); continue; }
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("  Invalid input.");
                }
            }

            existing.setTitle(title);
            existing.setCredit(credit);
            courseDAO.update(existing);
            System.out.println("Course updated successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteCourse() {
        try {
            int id = inputId("Course ID to delete");
            courseDAO.delete(id);
            System.out.println("Course deleted successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getCourseById() {
        try {
            int id = inputId("Course ID");
            Course c = courseDAO.getById(id);
            if (c == null) System.out.println("Course not found.");
            else System.out.println("Found: " + c);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void getAllCourses() {
        List<Course> list = courseDAO.getAll();
        if (list.isEmpty()) System.out.println("No courses found.");
        else list.forEach(System.out::println);
    }

    // ===================== ENROLLMENT MENU =====================
    private static void enrollMenu() {
        System.out.println("\n--- Enrollment Management ---");
        System.out.println("1. Enroll student in course");
        System.out.println("2. Unenroll student from course");
        System.out.println("3. View courses of a student");
        System.out.println("4. View students of a course");
        System.out.print("Your choice: ");
        switch (sc.nextLine().trim()) {
            case "1": enrollStudent();          break;
            case "2": unenrollStudent();        break;
            case "3": viewCoursesOfStudent();   break;
            case "4": viewStudentsOfCourse();   break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void enrollStudent() {
        try {
            int studentId = inputId("Student ID");
            int courseId  = inputId("Course ID");
            studentDAO.enroll(studentId, courseId);
            System.out.println("Enrolled successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void unenrollStudent() {
        try {
            int studentId = inputId("Student ID");
            int courseId  = inputId("Course ID");
            studentDAO.unenroll(studentId, courseId);
            System.out.println("Unenrolled successfully.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewCoursesOfStudent() {
        try {
            int studentId = inputId("Student ID");
            Set<Course> courses = studentDAO.getCoursesOfStudent(studentId);
            if (courses.isEmpty()) System.out.println("No courses found.");
            else {
                System.out.println("Courses:");
                courses.forEach(c -> System.out.println("  - " + c.getTitle()));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewStudentsOfCourse() {
        try {
            int courseId = inputId("Course ID");
            Set<Student> students = courseDAO.getStudentsOfCourse(courseId);
            if (students.isEmpty()) System.out.println("No students found.");
            else {
                System.out.println("Students:");
                students.forEach(s -> System.out.println("  - " + s.getName()));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== QUERY MENU (Task 5) =====================
    private static void queryMenu() {
        System.out.println("\n--- Queries ---");
        System.out.println("1. Find students older than (HQL)");
        System.out.println("2. List students with their courses (HQL Join)");
        System.out.println("3. Find student by name (Named Query)");
        System.out.println("4. Find courses with credit greater than (Criteria API)");
        System.out.println("5. Count students per course (Aggregation)");
        System.out.print("Your choice: ");
        switch (sc.nextLine().trim()) {
            case "1": queryOlderThan();          break;
            case "2": queryStudentsWithCourses(); break;
            case "3": queryByName();             break;
            case "4": queryByMinCredit();        break;
            case "5": queryCountPerCourse();     break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void queryOlderThan() {
        int age = inputId("Minimum age");
        List<Student> list = studentDAO.findOlderThan(age);
        System.out.println("Students older than " + age + ":");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(s -> System.out.println("  " + s));
    }

    private static void queryStudentsWithCourses() {
        List<Object[]> list = studentDAO.findStudentsWithCourses();
        System.out.println("Students and their courses:");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(row ->
                System.out.println("  Student: " + row[0] + " | Course: " + row[1]));
    }

    private static void queryByName() {
        System.out.print("Enter name to search: ");
        String name = sc.nextLine().trim();
        List<Student> list = studentDAO.findByName(name);
        System.out.println("Students with name '" + name + "':");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(s -> System.out.println("  " + s));
    }

    private static void queryByMinCredit() {
        int credit = inputId("Minimum credit");
        List<Course> list = courseDAO.findByMinCredit(credit);
        System.out.println("Courses with credit > " + credit + ":");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(c -> System.out.println("  " + c));
    }

    private static void queryCountPerCourse() {
        List<Object[]> list = studentDAO.countStudentsPerCourse();
        System.out.println("Student count per course:");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(row ->
                System.out.println("  " + row[0] + ": " + row[1] + " student(s)"));
    }

    // ===================== BONUS MENU =====================
    private static void bonusMenu() {
        System.out.println("\n--- Bonus ---");
        System.out.println("1. Students not enrolled in any course");
        System.out.println("2. Paginated list of students");
        System.out.print("Your choice: ");
        switch (sc.nextLine().trim()) {
            case "1": queryNotEnrolled(); break;
            case "2": queryPaginated();   break;
            default: System.out.println("Invalid choice.");
        }
    }

    private static void queryNotEnrolled() {
        List<Student> list = studentDAO.findStudentsNotEnrolled();
        System.out.println("Students not enrolled in any course:");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(s -> System.out.println("  " + s));
    }

    private static void queryPaginated() {
        int page = inputId("Page number");
        int size = inputId("Page size");
        List<Student> list = studentDAO.getAllPaginated(page, size);
        System.out.println("Page " + page + " (size " + size + "):");
        if (list.isEmpty()) System.out.println("  (none)");
        else list.forEach(s -> System.out.println("  " + s));
    }

    // ===================== HELPER =====================
    /**
     * Helper: nhập số nguyên dương với validation
     */
    private static int inputId(String label) {
        while (true) {
            System.out.print(label + ": ");
            try {
                int val = Integer.parseInt(sc.nextLine().trim());
                if (val <= 0) { System.out.println("  Must be positive."); continue; }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("  Invalid input. Please enter a number.");
            }
        }
    }
}