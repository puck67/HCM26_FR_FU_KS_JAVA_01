package com.practice;

import com.practice.config.HibernateUtil;
import com.practice.entity.Course;
import com.practice.entity.Student;
import com.practice.service.CenterService;
import com.practice.service.impl.CenterServiceImpl;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {

    private static final CenterService service = new CenterServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean running = true;

    public static void main(String[] args) {
        // Suppress Hibernate startup/logging outputs
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");

        System.out.println("Initializing Database Connection...");
        HibernateUtil.getSessionFactory(); // Trigger early initialization
        System.out.println("Database Connection initialized successfully.");

        // Automatically seed sample data if database is empty (Task 6 requirement)
        seedSampleDataIfEmpty();

        // Start the interactive lambda-based menu immediately
        runLambdaMenu();
    }

    private static void seedSampleDataIfEmpty() {
        try {
            if (service.getAllStudents().isEmpty() && service.getAllCourses().isEmpty()) {
                System.out.println("[Data Seeding] Database is empty. Seeding sample data (Task 6)...");
                
                service.createStudent("John Doe", 22);
                service.createStudent("Anna Smith", 20);
                service.createStudent("Bob Jones", 19);

                service.createCourse("Java Programming", 4);
                service.createCourse("Database Systems", 3);
                service.createCourse("Web Development", 2);

                // Fetch generated records to bind enrollments
                List<Student> students = service.getAllStudents();
                List<Course> courses = service.getAllCourses();

                if (students.size() >= 3 && courses.size() >= 3) {
                    int s1 = students.get(0).getId();
                    int s2 = students.get(1).getId();
                    int s3 = students.get(2).getId();

                    int c1 = courses.get(0).getId();
                    int c2 = courses.get(1).getId();
                    int c3 = courses.get(2).getId();

                    // Enrollments (Task 6)
                    service.enrollStudent(s1, c1);
                    service.enrollStudent(s1, c2);
                    service.enrollStudent(s2, c1);
                    service.enrollStudent(s2, c2);
                    service.enrollStudent(s2, c3);
                    service.enrollStudent(s3, c3);

                    System.out.println("[Data Seeding] Sample data successfully seeded.");
                }
            }
        } catch (Exception e) {
            System.err.println("[Data Seeding] Failed to seed sample data: " + e.getMessage());
        }
    }

    private static void runLambdaMenu() {
        Map<Integer, String> mainDescriptions = new LinkedHashMap<>();
        Map<Integer, Runnable> mainActions = new LinkedHashMap<>();

        mainDescriptions.put(1, "Student Management");
        mainDescriptions.put(2, "Course Management");
        mainDescriptions.put(3, "Enrollment & Query Practice");
        mainDescriptions.put(0, "Exit App");

        mainActions.put(1, Main::runStudentMenu);
        mainActions.put(2, Main::runCourseMenu);
        mainActions.put(3, Main::runQueryMenu);
        mainActions.put(0, () -> {
            System.out.println("Closing SessionFactory and Exiting application. Goodbye!");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        while (running) {
            System.out.println("\n========================================");
            System.out.println("            MAIN MENU");
            System.out.println("========================================");
            mainDescriptions.forEach((id, desc) -> System.out.printf("[%d] %s\n", id, desc));
            System.out.println("========================================");
            
            int choice = readIntInput("Enter your choice: ");
            Runnable action = mainActions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid selection. Try again.");
            }
        }
    }

    private static void runStudentMenu() {
        boolean studentMenuRunning = true;
        Map<Integer, String> menuDescriptions = new LinkedHashMap<>();
        Map<Integer, Runnable> menuActions = new LinkedHashMap<>();

        menuDescriptions.put(1, "Create a Student");
        menuDescriptions.put(2, "Update a Student");
        menuDescriptions.put(3, "Delete a Student");
        menuDescriptions.put(4, "Get Student by ID");
        menuDescriptions.put(5, "List All Students");
        menuDescriptions.put(0, "Back to Main Menu");

        menuActions.put(1, () -> {
            String name = readStringInput("Enter student name: ");
            int age = readIntInput("Enter student age: ");
            service.createStudent(name, age);
            System.out.println("Student created successfully!");
        });
        menuActions.put(2, () -> {
            // Validation: Only allow input if student directory is not empty
            if (!printAllStudentsSummary()) {
                System.out.println("Cannot update student. No records exist.");
                return;
            }
            int id = readIntInput("Enter student ID to update: ");
            String name = readStringInput("Enter new name: ");
            int age = readIntInput("Enter new age: ");
            if (service.updateStudent(id, name, age)) {
                System.out.println("Student updated successfully!");
            }
        });
        menuActions.put(3, () -> {
            // Validation: Only allow input if student directory is not empty
            if (!printAllStudentsSummary()) {
                System.out.println("Cannot delete student. No records exist.");
                return;
            }
            int id = readIntInput("Enter student ID to delete: ");
            if (service.deleteStudent(id)) {
                System.out.println("Student deleted successfully!");
            }
        });
        menuActions.put(4, () -> {
            // Validation: Only allow input if student directory is not empty
            if (!printAllStudentsSummary()) {
                System.out.println("Cannot fetch student. No records exist.");
                return;
            }
            int id = readIntInput("Enter student ID: ");
            Student s = service.getStudent(id);
            if (s != null) {
                System.out.println("Found: " + s);
                System.out.println("Courses: " + s.getCourses());
            } else {
                System.out.println("Student not found.");
            }
        });
        menuActions.put(5, () -> {
            List<Student> list = service.getAllStudents();
            if (list.isEmpty()) {
                System.out.println("No students found.");
            } else {
                list.forEach(System.out::println);
            }
        });

        while (studentMenuRunning) {
            System.out.println("\n----------------------------------------");
            System.out.println("        STUDENT MANAGEMENT");
            System.out.println("----------------------------------------");
            menuDescriptions.forEach((id, desc) -> System.out.printf("[%d] %s\n", id, desc));
            System.out.println("----------------------------------------");
            
            int choice = readIntInput("Enter choice: ");
            if (choice == 0) {
                studentMenuRunning = false;
            } else {
                Runnable action = menuActions.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            if (studentMenuRunning) {
                readStringInput("Press ENTER to continue...");
            }
        }
    }

    private static void runCourseMenu() {
        boolean courseMenuRunning = true;
        Map<Integer, String> menuDescriptions = new LinkedHashMap<>();
        Map<Integer, Runnable> menuActions = new LinkedHashMap<>();

        menuDescriptions.put(1, "Create a Course");
        menuDescriptions.put(2, "Update a Course");
        menuDescriptions.put(3, "Delete a Course");
        menuDescriptions.put(4, "List All Courses");
        menuDescriptions.put(0, "Back to Main Menu");

        menuActions.put(1, () -> {
            String title = readStringInput("Enter course title: ");
            int credit = readIntInput("Enter credit: ");
            service.createCourse(title, credit);
            System.out.println("Course created successfully!");
        });
        menuActions.put(2, () -> {
            // Validation: Only allow input if course directory is not empty
            if (!printAllCoursesSummary()) {
                System.out.println("Cannot update course. No records exist.");
                return;
            }
            int id = readIntInput("Enter course ID to update: ");
            String title = readStringInput("Enter new title: ");
            int credit = readIntInput("Enter new credit: ");
            if (service.updateCourse(id, title, credit)) {
                System.out.println("Course updated successfully!");
            }
        });
        menuActions.put(3, () -> {
            // Validation: Only allow input if course directory is not empty
            if (!printAllCoursesSummary()) {
                System.out.println("Cannot delete course. No records exist.");
                return;
            }
            int id = readIntInput("Enter course ID to delete: ");
            if (service.deleteCourse(id)) {
                System.out.println("Course deleted successfully!");
            }
        });
        menuActions.put(4, () -> {
            List<Course> list = service.getAllCourses();
            if (list.isEmpty()) {
                System.out.println("No courses found.");
            } else {
                list.forEach(System.out::println);
            }
        });

        while (courseMenuRunning) {
            System.out.println("\n----------------------------------------");
            System.out.println("        COURSE MANAGEMENT");
            System.out.println("----------------------------------------");
            menuDescriptions.forEach((id, desc) -> System.out.printf("[%d] %s\n", id, desc));
            System.out.println("----------------------------------------");
            
            int choice = readIntInput("Enter choice: ");
            if (choice == 0) {
                courseMenuRunning = false;
            } else {
                Runnable action = menuActions.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            if (courseMenuRunning) {
                readStringInput("Press ENTER to continue...");
            }
        }
    }

    private static void runQueryMenu() {
        boolean queryMenuRunning = true;
        Map<Integer, String> menuDescriptions = new LinkedHashMap<>();
        Map<Integer, Runnable> menuActions = new LinkedHashMap<>();

        menuDescriptions.put(1, "Enroll Student in Course");
        menuDescriptions.put(2, "Remove Student from Course");
        menuDescriptions.put(3, "Display all courses of a given student");
        menuDescriptions.put(4, "Display all students of a given course");
        menuDescriptions.put(5, "HQL: Find Students older than Age");
        menuDescriptions.put(6, "HQL Join: List Students and their Courses");
        menuDescriptions.put(7, "Named Query: Find Students by Name");
        menuDescriptions.put(8, "Criteria API: Find Courses with Credit > Value");
        menuDescriptions.put(9, "Aggregation: Count Students per Course");
        menuDescriptions.put(10, "List Students Paginated");
        menuDescriptions.put(11, "Find Unenrolled Students");
        menuDescriptions.put(0, "Back to Main Menu");

        menuActions.put(1, () -> {
            List<Student> students = service.getAllStudents();
            List<Course> courses = service.getAllCourses();
            if (students.isEmpty() || courses.isEmpty()) {
                System.out.println("[Notice] Enrollment requires at least one Student and one Course to exist.");
                return;
            }
            printAllStudentsSummary();
            int sId = readIntInput("Enter Student ID: ");
            printAllCoursesSummary();
            int cId = readIntInput("Enter Course ID: ");
            service.enrollStudent(sId, cId);
            System.out.println("Enrollment completed.");
        });
        menuActions.put(2, () -> {
            List<Student> students = service.getAllStudents();
            List<Course> courses = service.getAllCourses();
            if (students.isEmpty() || courses.isEmpty()) {
                System.out.println("[Notice] Disenrollment requires at least one Student and one Course to exist.");
                return;
            }
            printAllStudentsSummary();
            int sId = readIntInput("Enter Student ID: ");
            printAllCoursesSummary();
            int cId = readIntInput("Enter Course ID: ");
            service.removeStudentFromCourse(sId, cId);
            System.out.println("Disenrollment completed.");
        });
        menuActions.put(3, () -> {
            if (!printAllStudentsSummary()) {
                System.out.println("No students available to inspect.");
                return;
            }
            int sId = readIntInput("Enter Student ID: ");
            List<Course> courses = service.getCoursesOfStudent(sId);
            if (courses.isEmpty()) {
                System.out.println("Not enrolled in any courses.");
            } else {
                courses.forEach(System.out::println);
            }
        });
        menuActions.put(4, () -> {
            if (!printAllCoursesSummary()) {
                System.out.println("No courses available to inspect.");
                return;
            }
            int cId = readIntInput("Enter Course ID: ");
            List<Student> students = service.getStudentsOfCourse(cId);
            if (students.isEmpty()) {
                System.out.println("No students enrolled.");
            } else {
                students.forEach(System.out::println);
            }
        });
        menuActions.put(5, () -> {
            int age = readIntInput("Enter minimum age: ");
            List<Student> results = service.getStudentsOlderThan(age);
            if (results.isEmpty()) {
                System.out.println("No students found older than " + age + ".");
            } else {
                results.forEach(System.out::println);
            }
        });
        menuActions.put(6, () -> {
            List<Object[]> results = service.getStudentsAndCourses();
            if (results.isEmpty()) {
                System.out.println("No enrollments found.");
            } else {
                results.forEach(row -> {
                    Student s = (Student) row[0];
                    Course c = (Course) row[1];
                    System.out.println(s.getName() + " -> " + c.getTitle());
                });
            }
        });
        menuActions.put(7, () -> {
            String name = readStringInput("Enter name: ");
            List<Student> results = service.getStudentsByName(name);
            if (results.isEmpty()) {
                System.out.println("No students found matching '" + name + "'.");
            } else {
                results.forEach(System.out::println);
            }
        });
        menuActions.put(8, () -> {
            int credit = readIntInput("Enter minimum credits: ");
            List<Course> results = service.getCoursesWithCreditGreaterThan(credit);
            if (results.isEmpty()) {
                System.out.println("No courses found with credits greater than " + credit + ".");
            } else {
                results.forEach(System.out::println);
            }
        });
        menuActions.put(9, () -> {
            Map<String, Long> results = service.getStudentCountPerCourse();
            if (results.isEmpty()) {
                System.out.println("No student count data available.");
            } else {
                results.forEach((title, count) -> System.out.println(title + ": " + count));
            }
        });
        menuActions.put(10, () -> {
            int page = readIntInput("Enter page number: ");
            int size = readIntInput("Enter page size: ");
            List<Student> results = service.getAllStudentsPaginated(page, size);
            if (results.isEmpty()) {
                System.out.println("No student records found on page " + page + " (size " + size + ").");
            } else {
                results.forEach(System.out::println);
            }
        });
        menuActions.put(11, () -> {
            List<Student> results = service.getUnenrolledStudents();
            if (results.isEmpty()) {
                System.out.println("All students are enrolled in at least one course.");
            } else {
                results.forEach(System.out::println);
            }
        });

        while (queryMenuRunning) {
            System.out.println("\n----------------------------------------");
            System.out.println("        ENROLLMENT & QUERIES");
            System.out.println("----------------------------------------");
            menuDescriptions.forEach((id, desc) -> System.out.printf("[%d] %s\n", id, desc));
            System.out.println("----------------------------------------");
            
            int choice = readIntInput("Enter choice: ");
            if (choice == 0) {
                queryMenuRunning = false;
            } else {
                Runnable action = menuActions.get(choice);
                if (action != null) {
                    action.run();
                } else {
                    System.out.println("Invalid choice.");
                }
            }
            if (queryMenuRunning) {
                readStringInput("Press ENTER to continue...");
            }
        }
    }

    private static boolean printAllStudentsSummary() {
        List<Student> list = service.getAllStudents();
        if (list.isEmpty()) {
            System.out.println("[Notice] No students found.");
            return false;
        }
        System.out.println("--- Student Directory ---");
        list.forEach(s -> System.out.printf("ID: %d | Name: %s | Age: %d\n", s.getId(), s.getName(), s.getAge()));
        System.out.println("-------------------------");
        return true;
    }

    private static boolean printAllCoursesSummary() {
        List<Course> list = service.getAllCourses();
        if (list.isEmpty()) {
            System.out.println("[Notice] No courses found.");
            return false;
        }
        System.out.println("--- Course Catalog ---");
        list.forEach(c -> System.out.printf("ID: %d | Title: %s | Credits: %d\n", c.getId(), c.getTitle(), c.getCredit()));
        System.out.println("----------------------");
        return true;
    }

    private static String readStringInput(String prompt) {
        System.out.print(prompt);
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine().trim();
            }
        } catch (Exception e) {
            System.out.println("\n[Error] Failed to read input.");
        }
        return "";
    }

    private static int readIntInput(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim();
                    if (input.isEmpty()) {
                        System.out.print("Input cannot be empty. Enter an integer: ");
                        continue;
                    }
                    return Integer.parseInt(input);
                } else {
                    return 0; // Stream closed
                }
            } catch (NumberFormatException e) {
                System.out.print("Invalid format. Enter an integer: ");
            } catch (Exception e) {
                System.out.print("Error reading input. Enter an integer: ");
            }
        }
    }
}
