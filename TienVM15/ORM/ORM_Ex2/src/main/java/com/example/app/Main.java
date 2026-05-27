package com.example.app;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.CourseService;
import com.example.service.StudentService;
import com.example.service.impl.CourseServiceImpl;
import com.example.service.impl.StudentServiceImpl;
import com.example.util.HibernateUtil;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Main entry point for the Training Center CLI application.
 * Uses lambda-based menus (via MenuManager) for Student, Course, Enrollment & Queries.
 */
public class Main {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static final MenuManager menu = new MenuManager(scanner);

    public static void main(String[] args) {
        System.out.println("Initializing Database Connection...");
        HibernateUtil.getSessionFactory(); // Trigger early initialization
        System.out.println("Database Connection initialized successfully.");

        // Auto-seed sample data if database is empty
        seedSampleDataIfEmpty();

        // Build and run the main menu
        runMainMenu();
    }

    // -------------------------------------------------------------------------
    // Data Seeding
    // -------------------------------------------------------------------------

    private static void seedSampleDataIfEmpty() {
        try {
            if (studentService.getAllStudents().isEmpty() && courseService.getAllCourses().isEmpty()) {
                System.out.println("[Data Seeding] Database is empty. Seeding sample data...");

                studentService.createStudent("John Doe", 22);
                studentService.createStudent("Anna Smith", 20);
                studentService.createStudent("Bob Jones", 19);

                courseService.createCourse("Java Programming", 4);
                courseService.createCourse("Database Systems", 3);
                courseService.createCourse("Web Development", 2);

                List<Student> students = studentService.getAllStudents();
                List<Course> courses = courseService.getAllCourses();

                if (students.size() >= 3 && courses.size() >= 3) {
                    int s1 = students.get(0).getId();
                    int s2 = students.get(1).getId();
                    int s3 = students.get(2).getId();
                    int c1 = courses.get(0).getId();
                    int c2 = courses.get(1).getId();
                    int c3 = courses.get(2).getId();

                    courseService.enrollStudent(s1, c1);
                    courseService.enrollStudent(s1, c2);
                    courseService.enrollStudent(s2, c1);
                    courseService.enrollStudent(s2, c2);
                    courseService.enrollStudent(s2, c3);
                    courseService.enrollStudent(s3, c3);

                    System.out.println("[Data Seeding] Sample data successfully seeded.");
                }
            }
        } catch (Exception e) {
            System.err.println("[Data Seeding] Failed: " + e.getMessage());
        }
    }

    // -------------------------------------------------------------------------
    // Main Menu
    // -------------------------------------------------------------------------

    private static void runMainMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Student Management");
        descriptions.put(2, "Course Management");
        descriptions.put(3, "Enrollment Management");
        descriptions.put(4, "Queries & Reports");
        descriptions.put(0, "Exit App");

        actions.put(1, Main::runStudentMenu);
        actions.put(2, Main::runCourseMenu);
        actions.put(3, Main::runEnrollmentMenu);
        actions.put(4, Main::runQueryMenu);
        actions.put(0, () -> {
            System.out.println("Closing SessionFactory and exiting. Goodbye!");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        // showContinuePrompt=false: actions here are submenus, no "Press ENTER" needed between visits
        menu.runMenu("TRAINING CENTER - MAIN MENU", descriptions, actions, false);
    }

    // -------------------------------------------------------------------------
    // Student Management Submenu
    // -------------------------------------------------------------------------

    private static void runStudentMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Create a Student");
        descriptions.put(2, "Update a Student");
        descriptions.put(3, "Delete a Student");
        descriptions.put(4, "Get Student by ID");
        descriptions.put(5, "List All Students");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            String name = menu.readString("Enter student name: ");
            int age = menu.readInt("Enter student age: ");
            studentService.createStudent(name, age);
            System.out.println("Student created successfully!");
        });
        actions.put(2, () -> {
            if (!printStudentsSummary()) return;
            int id = menu.readInt("Enter student ID to update: ");
            String name = menu.readString("Enter new name: ");
            int age = menu.readInt("Enter new age: ");
            studentService.updateStudent(id, name, age);
        });
        actions.put(3, () -> {
            if (!printStudentsSummary()) return;
            int id = menu.readInt("Enter student ID to delete: ");
            if (studentService.deleteStudent(id)) {
                System.out.println("Student deleted successfully!");
            }
        });
        actions.put(4, () -> {
            if (!printStudentsSummary()) return;
            int id = menu.readInt("Enter student ID: ");
            Student s = studentService.getStudent(id);
            if (s != null) {
                displayStudents("Student Details", List.of(s));
                List<Course> courses = new java.util.ArrayList<>(s.getCourses());
                displayCourses("Enrolled Courses", courses);
            } else {
                System.out.println("❌ Student not found.");
            }
        });
        actions.put(5, () -> {
            List<Student> list = studentService.getAllStudents();
            if (list.isEmpty()) {
                System.out.println("❌ No students found.");
            } else {
                displayStudents("All Students", list);
            }
        });

        menu.runMenu("STUDENT MANAGEMENT", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Course Management Submenu
    // -------------------------------------------------------------------------

    private static void runCourseMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Create a Course");
        descriptions.put(2, "Update a Course");
        descriptions.put(3, "Delete a Course");
        descriptions.put(4, "List All Courses");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            String title = menu.readString("Enter course title: ");
            int credit = menu.readInt("Enter credit: ");
            courseService.createCourse(title, credit);
            System.out.println("Course created successfully!");
        });
        actions.put(2, () -> {
            if (!printCoursesSummary()) return;
            int id = menu.readInt("Enter course ID to update: ");
            String title = menu.readString("Enter new title: ");
            int credit = menu.readInt("Enter new credit: ");
            courseService.updateCourse(id, title, credit);
        });
        actions.put(3, () -> {
            if (!printCoursesSummary()) return;
            int id = menu.readInt("Enter course ID to delete: ");
            if (courseService.deleteCourse(id)) {
                System.out.println("Course deleted successfully!");
            }
        });
        actions.put(4, () -> {
            List<Course> list = courseService.getAllCourses();
            if (list.isEmpty()) {
                System.out.println("❌ No courses found.");
            } else {
                displayCourses("All Courses", list);
            }
        });

        menu.runMenu("COURSE MANAGEMENT", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Enrollment Management Submenu
    // -------------------------------------------------------------------------

    private static void runEnrollmentMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "Enroll Student in Course");
        descriptions.put(2, "Remove Student from Course");
        descriptions.put(3, "Display Courses of a Student");
        descriptions.put(4, "Display Students of a Course");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            if (studentService.getAllStudents().isEmpty() || courseService.getAllCourses().isEmpty()) {
                System.out.println("[Notice] Enrollment requires at least one Student and one Course.");
                return;
            }
            printStudentsSummary();
            int sId = menu.readInt("Enter Student ID: ");
            printCoursesSummary();
            int cId = menu.readInt("Enter Course ID: ");
            courseService.enrollStudent(sId, cId);
            System.out.println("Enrollment completed.");
        });
        actions.put(2, () -> {
            if (studentService.getAllStudents().isEmpty() || courseService.getAllCourses().isEmpty()) {
                System.out.println("[Notice] Disenrollment requires at least one Student and one Course.");
                return;
            }
            printStudentsSummary();
            int sId = menu.readInt("Enter Student ID: ");
            printCoursesSummary();
            int cId = menu.readInt("Enter Course ID: ");
            courseService.removeStudentFromCourse(sId, cId);
            System.out.println("Disenrollment completed.");
        });
        actions.put(3, () -> {
            if (!printStudentsSummary()) return;
            int sId = menu.readInt("Enter Student ID: ");
            List<Course> courses = courseService.getCoursesOfStudent(sId);
            if (courses.isEmpty()) {
                System.out.println("⚠️ Not enrolled in any courses.");
            } else {
                displayCourses("Enrolled Courses", courses);
            }
        });
        actions.put(4, () -> {
            if (!printCoursesSummary()) return;
            int cId = menu.readInt("Enter Course ID: ");
            List<Student> students = courseService.getStudentsOfCourse(cId);
            if (students.isEmpty()) {
                System.out.println("⚠️ No students enrolled.");
            } else {
                displayStudents("Enrolled Students", students);
            }
        });

        menu.runMenu("ENROLLMENT MANAGEMENT", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Queries & Reports Submenu
    // -------------------------------------------------------------------------

    private static void runQueryMenu() {
        Map<Integer, String> descriptions = new LinkedHashMap<>();
        Map<Integer, MenuAction> actions = new LinkedHashMap<>();

        descriptions.put(1, "HQL: Find Students older than Age");
        descriptions.put(2, "HQL Join: List Students and their Courses");
        descriptions.put(3, "Named Query: Find Students by Name");
        descriptions.put(4, "Criteria API: Find Courses with Credit > Value");
        descriptions.put(5, "Aggregation: Count Students per Course");
        descriptions.put(6, "Paginated: List Students");
        descriptions.put(7, "Find Unenrolled Students");
        descriptions.put(0, "Back to Main Menu");

        actions.put(1, () -> {
            int age = menu.readInt("Enter minimum age: ");
            List<Student> results = studentService.getStudentsOlderThan(age);
            if (results.isEmpty()) {
                System.out.println("⚠️ No students older than " + age + ".");
            } else {
                displayStudents("Students Older Than " + age, results);
            }
        });
        actions.put(2, () -> {
            List<Object[]> results = studentService.getStudentsAndCourses();
            if (results.isEmpty()) {
                System.out.println("⚠️ No enrollments found.");
            } else {
                List<String> headers = List.of("Student Name", "Course Title");
                List<List<String>> rows = new java.util.ArrayList<>();
                for (Object[] row : results) {
                    Student s = (Student) row[0];
                    Course c = (Course) row[1];
                    rows.add(List.of(s.getName(), c.getTitle()));
                }
                com.example.util.TableRenderer.printTable("Students & Enrolled Courses (HQL Join)", headers, rows);
            }
        });
        actions.put(3, () -> {
            String name = menu.readString("Enter name keyword: ");
            List<Student> results = studentService.getStudentsByName(name);
            if (results.isEmpty()) {
                System.out.println("⚠️ No students matching '" + name + "'.");
            } else {
                displayStudents("Search Results for '" + name + "'", results);
            }
        });
        actions.put(4, () -> {
            int credit = menu.readInt("Enter minimum credits: ");
            List<Course> results = courseService.getCoursesWithCreditGreaterThan(credit);
            if (results.isEmpty()) {
                System.out.println("⚠️ No courses with credits > " + credit + ".");
            } else {
                displayCourses("Courses with Credit > " + credit, results);
            }
        });
        actions.put(5, () -> {
            Map<String, Long> results = courseService.getStudentCountPerCourse();
            if (results.isEmpty()) {
                System.out.println("⚠️ No data available.");
            } else {
                List<String> headers = List.of("Course Title", "Student Count");
                List<List<String>> rows = new java.util.ArrayList<>();
                results.forEach((title, count) -> rows.add(List.of(title, String.valueOf(count))));
                com.example.util.TableRenderer.printTable("Student Count Per Course", headers, rows);
            }
        });
        actions.put(6, () -> {
            int page = menu.readInt("Enter page number (starts at 1): ");
            int size = menu.readInt("Enter page size: ");
            List<Student> results = studentService.getAllStudentsPaginated(page, size);
            if (results.isEmpty()) {
                System.out.println("⚠️ No students on page " + page + " (size " + size + ").");
            } else {
                displayStudents("Students Page " + page + " (Size " + size + ")", results);
            }
        });
        actions.put(7, () -> {
            List<Student> results = studentService.getUnenrolledStudents();
            if (results.isEmpty()) {
                System.out.println("🎉 All students are enrolled in at least one course.");
            } else {
                displayStudents("Unenrolled Students", results);
            }
        });

        menu.runMenu("QUERIES & REPORTS", descriptions, actions);
    }

    // -------------------------------------------------------------------------
    // Helper Printers & Display Formatting
    // -------------------------------------------------------------------------

    private static void displayStudents(String title, List<Student> students) {
        List<String> headers = List.of("Student ID", "Full Name", "Age");
        List<List<String>> rows = new java.util.ArrayList<>();
        for (Student s : students) {
            rows.add(List.of(String.valueOf(s.getId()), s.getName(), String.valueOf(s.getAge())));
        }
        com.example.util.TableRenderer.printTable(title, headers, rows);
    }

    private static void displayCourses(String title, List<Course> courses) {
        List<String> headers = List.of("Course ID", "Course Title", "Credits");
        List<List<String>> rows = new java.util.ArrayList<>();
        for (Course c : courses) {
            rows.add(List.of(String.valueOf(c.getId()), c.getTitle(), String.valueOf(c.getCredit())));
        }
        com.example.util.TableRenderer.printTable(title, headers, rows);
    }

    private static boolean printStudentsSummary() {
        List<Student> list = studentService.getAllStudents();
        if (list.isEmpty()) {
            System.out.println("⚠️ [Notice] No students found.");
            return false;
        }
        displayStudents("Student Directory", list);
        return true;
    }

    private static boolean printCoursesSummary() {
        List<Course> list = courseService.getAllCourses();
        if (list.isEmpty()) {
            System.out.println("⚠️ [Notice] No courses found.");
            return false;
        }
        displayCourses("Course Catalog", list);
        return true;
    }
}
