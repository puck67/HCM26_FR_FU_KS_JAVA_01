package fa.training;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.services.EnrollmentService;
import fa.training.services.QueryService;
import fa.training.utils.HibernateUtil;

import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final StudentDAO studentDAO = new StudentDAO();
    private static final CourseDAO courseDAO = new CourseDAO();
    private static final EnrollmentService enrollmentService = new EnrollmentService();
    private static final QueryService queryService = new QueryService();

    static {
        // Silence JUL (Java Util Logging) at INFO level (including Hibernate and HikariCP startup logs)
        System.setProperty("org.jboss.logging.provider", "slf4j");
        java.util.logging.Logger.getLogger("").setLevel(java.util.logging.Level.WARNING);
    }

    public static void main(String[] args) {
        try {
            while (true) {
                System.out.println("\n==================================================");
                System.out.println("      TRAINING MANAGEMENT SYSTEM (HIBERNATE)");
                System.out.println("==================================================");
                System.out.println("1. Student Management");
                System.out.println("2. Course Management");
                System.out.println("3. Enrollment Management");
                System.out.println("4. Run Automated Test (Task 5 & 6)");
                System.out.println("5. Exit");
                System.out.println("==================================================");
                System.out.print("Select an option (1-5): ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1:
                        handleStudentManagement();
                        break;
                    case 2:
                        handleCourseManagement();
                        break;
                    case 3:
                        handleEnrollmentManagement();
                        break;
                    case 4:
                        runAutomatedTests();
                        break;
                    case 5:
                        System.out.println("Exiting program...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again!");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            HibernateUtil.shutdown();
        }
    }

    private static void handleStudentManagement() {
        while (true) {
            System.out.println("\n--- Student Management Submenu ---");
            System.out.println("1. Create Student");
            System.out.println("2. Get Student by ID");
            System.out.println("3. Get All Students");
            System.out.println("4. Update Student");
            System.out.println("5. Delete Student");
            System.out.println("6. Back to main menu");
            System.out.print("Select an option (1-6): ");

            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    System.out.print("Enter Student Name: ");
                    String name = scanner.nextLine();
                    System.out.print("Enter Student Age: ");
                    int age = Integer.parseInt(scanner.nextLine());
                    studentDAO.create(new Student(name, age));
                    System.out.println("Student created successfully!");
                    break;
                case 2:
                    System.out.print("Enter Student ID: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    Student s = studentDAO.getById(id);
                    if (s != null) {
                        System.out.println("Found: " + s);
                    } else {
                        System.out.println("Student not found!");
                    }
                    break;
                case 3:
                    System.out.println("All Students:");
                    studentDAO.getAll().forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("Enter Student ID to update: ");
                    Long updateId = Long.parseLong(scanner.nextLine());
                    Student stu = studentDAO.getById(updateId);
                    if (stu != null) {
                        System.out.print("Enter new Name (current: " + stu.getName() + "): ");
                        String newName = scanner.nextLine();
                        System.out.print("Enter new Age (current: " + stu.getAge() + "): ");
                        int newAge = Integer.parseInt(scanner.nextLine());
                        stu.setName(newName);
                        stu.setAge(newAge);
                        studentDAO.update(stu);
                        System.out.println("Student updated successfully!");
                    } else {
                        System.out.println("Student not found!");
                    }
                    break;
                case 5:
                    System.out.print("Enter Student ID to delete: ");
                    Long deleteId = Long.parseLong(scanner.nextLine());
                    studentDAO.delete(deleteId);
                    System.out.println("Delete action completed.");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleCourseManagement() {
        while (true) {
            System.out.println("\n--- Course Management Submenu ---");
            System.out.println("1. Create Course");
            System.out.println("2. Get Course by ID");
            System.out.println("3. Get All Courses");
            System.out.println("4. Update Course");
            System.out.println("5. Delete Course");
            System.out.println("6. Back to main menu");
            System.out.print("Select an option (1-6): ");

            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    System.out.print("Enter Course Title: ");
                    String title = scanner.nextLine();
                    System.out.print("Enter Course Credit: ");
                    int credit = Integer.parseInt(scanner.nextLine());
                    courseDAO.create(new Course(title, credit));
                    System.out.println("Course created successfully!");
                    break;
                case 2:
                    System.out.print("Enter Course ID: ");
                    Long id = Long.parseLong(scanner.nextLine());
                    Course c = courseDAO.getById(id);
                    if (c != null) {
                        System.out.println("Found: " + c);
                    } else {
                        System.out.println("Course not found!");
                    }
                    break;
                case 3:
                    System.out.println("All Courses:");
                    courseDAO.getAll().forEach(System.out::println);
                    break;
                case 4:
                    System.out.print("Enter Course ID to update: ");
                    Long updateId = Long.parseLong(scanner.nextLine());
                    Course crs = courseDAO.getById(updateId);
                    if (crs != null) {
                        System.out.print("Enter new Title (current: " + crs.getTitle() + "): ");
                        String newTitle = scanner.nextLine();
                        System.out.print("Enter new Credit (current: " + crs.getCredit() + "): ");
                        int newCredit = Integer.parseInt(scanner.nextLine());
                        crs.setTitle(newTitle);
                        crs.setCredit(newCredit);
                        courseDAO.update(crs);
                        System.out.println("Course updated successfully!");
                    } else {
                        System.out.println("Course not found!");
                    }
                    break;
                case 5:
                    System.out.print("Enter Course ID to delete: ");
                    Long deleteId = Long.parseLong(scanner.nextLine());
                    courseDAO.delete(deleteId);
                    System.out.println("Delete action completed.");
                    break;
                case 6:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleEnrollmentManagement() {
        while (true) {
            System.out.println("\n--- Enrollment Management Submenu ---");
            System.out.println("1. Enroll Student in Course");
            System.out.println("2. Remove Student from Course");
            System.out.println("3. Display Courses of Student");
            System.out.println("4. Display Students of Course");
            System.out.println("5. Back to main menu");
            System.out.print("Select an option (1-5): ");

            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    System.out.print("Enter Student ID: ");
                    Long studentId = Long.parseLong(scanner.nextLine());
                    System.out.print("Enter Course ID: ");
                    Long courseId = Long.parseLong(scanner.nextLine());
                    enrollmentService.enrollStudentInCourse(studentId, courseId);
                    break;
                case 2:
                    System.out.print("Enter Student ID: ");
                    Long sId = Long.parseLong(scanner.nextLine());
                    System.out.print("Enter Course ID: ");
                    Long cId = Long.parseLong(scanner.nextLine());
                    enrollmentService.removeStudentFromCourse(sId, cId);
                    break;
                case 3:
                    System.out.print("Enter Student ID: ");
                    Long studId = Long.parseLong(scanner.nextLine());
                    enrollmentService.displayCoursesOfStudent(studId);
                    break;
                case 4:
                    System.out.print("Enter Course ID: ");
                    Long courId = Long.parseLong(scanner.nextLine());
                    enrollmentService.displayStudentsOfCourse(courId);
                    break;
                case 5:
                    return;
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void runAutomatedTests() {
        System.out.println("\n====== TASK 6: AUTOMATED TEST PROGRAM ======\n");


        System.out.println("--- Step 1: Inserting Sample Data ---");
        Student s1 = new Student("John", 19);
        Student s2 = new Student("Anna", 22);
        Student s3 = new Student("Peter", 21);
        Student s4 = new Student("Mary", 20);

        studentDAO.create(s1);
        studentDAO.create(s2);
        studentDAO.create(s3);
        studentDAO.create(s4);

        Course c1 = new Course("Java Programming", 4);
        Course c2 = new Course("Database Systems", 3);
        Course c3 = new Course("Web Development", 3);

        courseDAO.create(c1);
        courseDAO.create(c2);
        courseDAO.create(c3);
        System.out.println("Inserted: 4 Students, 3 Courses.\n");

        // --- Step 2: Enroll students in courses ---
        System.out.println("--- Step 2: Enrolling Students in Courses ---");
        enrollmentService.enrollStudentInCourse(s1.getId(), c1.getId()); // John -> Java Programming
        enrollmentService.enrollStudentInCourse(s1.getId(), c2.getId()); // John -> Database Systems
        enrollmentService.enrollStudentInCourse(s2.getId(), c1.getId()); // Anna -> Java Programming
        enrollmentService.enrollStudentInCourse(s2.getId(), c3.getId()); // Anna -> Web Development
        enrollmentService.enrollStudentInCourse(s3.getId(), c2.getId()); // Peter -> Database Systems
        System.out.println();

        // --- Step 3: Task 5 Queries ---
        System.out.println("--- Step 3: Task 5 Queries ---");

        System.out.println("[Query 1 - HQL] Students older than 20:");
        queryService.findStudentsOlderThan(20).forEach(System.out::println);

        System.out.println("\n[Query 2 - HQL Join] Students and their enrolled courses:");
        queryService.listStudentsAndCourses();

        System.out.println("\n[Query 3 - Named Query] Find student by name 'Anna':");
        queryService.findStudentsByName("Anna").forEach(System.out::println);

        System.out.println("\n[Query 4 - Criteria API] Courses with credit > 3:");
        queryService.findCoursesWithCreditGreaterThan(3).forEach(System.out::println);

        System.out.println("\n[Query 5 - Aggregation] Student count per course:");
        queryService.countStudentsInEachCourse();

        System.out.println("\n====== TEST COMPLETED ======");
    }
}