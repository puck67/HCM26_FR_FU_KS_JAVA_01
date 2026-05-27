package fa.training.main;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.services.EnrollmentService;
import fa.training.utils.HibernateUtils;

import java.util.*;

public class App {

    private static final Scanner scanner = new Scanner(System.in);
    private static final EnrollmentService enrollmentService = new EnrollmentService();

    public static void main(String[] args) {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "error");
        System.setProperty("org.jboss.logging.provider", "slf4j");
        java.util.logging.Logger.getLogger("org.hibernate").setLevel(java.util.logging.Level.SEVERE);
        while (true) {
            System.out.println("\n==========================================");
            System.out.println("   HIBERNATE CRUD & QUERY PRACTICE MENU   ");
            System.out.println("==========================================");
            System.out.println("1. Insert Sample Data");
            System.out.println("2. Student CRUD Operations");
            System.out.println("3. Course CRUD Operations");
            System.out.println("4. Student Enrollment Management");
            System.out.println("5. Task 5 Queries (HQL, Named Query, Criteria)");
            System.out.println("6. Bonus Queries (Pagination & Free Students)");
            System.out.println("7. Exit");
            System.out.print("Choose an option: ");

            int choice = readIntInput();
            switch (choice) {
                case 1 -> handleInsertSampleData();
                case 2 -> handleStudentCRUD();
                case 3 -> handleCourseCRUD();
                case 4 -> handleEnrollment();
                case 5 -> handleQueryPractice();
                case 6 -> handleBonusQueries();
                case 7 -> {
                    System.out.println("Closing Hibernate...");
                    HibernateUtils.shutdown();
                    System.out.println("Goodbye!");
                    System.exit(0);
                }
                default -> System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static int readIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
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

    private static String readNameInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.matches("^[a-zA-Z\\s]+$")) {
                return input;
            }
            System.out.println("Invalid name. Name must contain only letters and spaces, and cannot be empty.");
        }
    }

    private static String readNonEmptyString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("Input cannot be empty.");
        }
    }

    private static void handleInsertSampleData() {
        System.out.println("\n--- [1] Inserting Sample Data ---");
        Student s1 = new Student("John", 22);
        Student s2 = new Student("Anna", 25);
        Student s3 = new Student("Bob", 19);
        Student s4 = new Student("Charlie", 20);

        enrollmentService.saveStudent(s1);
        enrollmentService.saveStudent(s2);
        enrollmentService.saveStudent(s3);
        enrollmentService.saveStudent(s4);

        Course c1 = new Course("Java Programming", 4);
        Course c2 = new Course("Database Systems", 3);
        Course c3 = new Course("Web Development", 3);
        Course c4 = new Course("Introduction to Art", 1);

        enrollmentService.saveCourse(c1);
        enrollmentService.saveCourse(c2);
        enrollmentService.saveCourse(c3);
        enrollmentService.saveCourse(c4);

        System.out.println("Sample Data Loaded successfully!");
    }

    private static void handleStudentCRUD() {
        System.out.println("\n--- [2] Student CRUD Operations ---");
        System.out.println("1. Create Student");
        System.out.println("2. Read Student by ID");
        System.out.println("3. Update Student Info");
        System.out.println("4. Delete Student");
        System.out.println("5. List All Students");
        System.out.print("Select action: ");
        int choice = readIntInput();

        if (choice == 1) {
            String name = readNameInput("Enter Student Name: ");
            int age = readPositiveInt("Enter Student Age: ");
            Student s = new Student(name, age);
            enrollmentService.saveStudent(s);
            System.out.println("Student created: " + s);
        } else if (choice == 2) {
            int id = readPositiveInt("Enter Student ID: ");
            Student s = enrollmentService.getStudentById(id);
            System.out.println("Result: " + s);
        } else if (choice == 3) {
            int id = readPositiveInt("Enter Student ID to update: ");
            Student s = enrollmentService.getStudentById(id);
            if (s != null) {
                String name = readNameInput("Enter New Name (" + s.getName() + "): ");
                int age = readPositiveInt("Enter New Age (" + s.getAge() + "): ");
                s.setName(name);
                s.setAge(age);
                enrollmentService.updateStudent(s);
                System.out.println("Student updated successfully.");
            } else {
                System.out.println("Student not found.");
            }
        } else if (choice == 4) {
            int id = readPositiveInt("Enter Student ID to delete: ");
            enrollmentService.deleteStudent(id);
            System.out.println("Delete action completed.");
        } else if (choice == 5) {
            List<Student> list = enrollmentService.getAllStudents();
            list.forEach(System.out::println);
        }
    }

    private static void handleCourseCRUD() {
        System.out.println("\n--- [3] Course CRUD Operations ---");
        System.out.println("1. Create Course");
        System.out.println("2. List All Courses");
        System.out.println("3. Delete Course");
        System.out.print("Select action: ");
        int choice = readIntInput();

        if (choice == 1) {
            String title = readNonEmptyString("Enter Course Title: ");
            int credit = readPositiveInt("Enter Credit: ");
            Course c = new Course(title, credit);
            enrollmentService.saveCourse(c);
            System.out.println("Course created: " + c);
        } else if (choice == 2) {
            List<Course> list = enrollmentService.getAllCourses();
            list.forEach(System.out::println);
        } else if (choice == 3) {
            int id = readPositiveInt("Enter Course ID to delete: ");
            enrollmentService.deleteCourse(id);
            System.out.println("Delete action completed.");
        }
    }

    private static void handleEnrollment() {
        System.out.println("\n--- [4] Enrollment Management ---");
        System.out.println("1. Enroll Student in Course");
        System.out.println("2. Remove Student from Course");
        System.out.println("3. View Courses of a Student");
        System.out.println("4. View Students of a Course");
        System.out.print("Select action: ");
        int choice = readIntInput();

        if (choice == 1) {
            int sId = readPositiveInt("Enter Student ID: ");
            int cId = readPositiveInt("Enter Course ID: ");
            enrollmentService.enrollStudent(sId, cId);
            System.out.println("Enrollment request processed.");
        } else if (choice == 2) {
            int sId = readPositiveInt("Enter Student ID: ");
            int cId = readPositiveInt("Enter Course ID: ");
            enrollmentService.removeStudentFromCourse(sId, cId);
            System.out.println("Removal request processed.");
        } else if (choice == 3) {
            int sId = readPositiveInt("Enter Student ID: ");
            Set<Course> courses = enrollmentService.getCoursesByStudent(sId);
            if (courses.isEmpty()) {
                System.out.println("No courses enrolled or student not found.");
            } else {
                courses.forEach(c -> System.out.println("  - " + c.getTitle() + " (Credit: " + c.getCredit() + ")"));
            }
        } else if (choice == 4) {
            int cId = readPositiveInt("Enter Course ID: ");
            Set<Student> students = enrollmentService.getStudentsByCourse(cId);
            if (students.isEmpty()) {
                System.out.println("No students enrolled or course not found.");
            } else {
                students.forEach(s -> System.out.println("  - " + s.getName() + " (Age: " + s.getAge() + ")"));
            }
        }
    }

    private static void handleQueryPractice() {
        System.out.println("\n--- [5] Task 5 Queries ---");
        System.out.println("1. HQL: Find students older than given age");
        System.out.println("2. HQL with Join: List students and their enrolled courses");
        System.out.println("3. Named Query: Find students by name");
        System.out.println("4. Criteria API: Find courses with credit greater than given value");
        System.out.println("5. Aggregation: Count how many students are enrolled in each course");
        System.out.print("Select query: ");
        int choice = readIntInput();

        if (choice == 1) {
            int age = readPositiveInt("Enter Age: ");
            List<Student> result = enrollmentService.findStudentsOlderThan(age);
            if (result.isEmpty()) {
                System.out.println("No students match criteria.");
            } else {
                result.forEach(System.out::println);
            }
        } else if (choice == 2) {
            List<Student> list = enrollmentService.getStudentsAndCourses();
            if (list.isEmpty()) {
                System.out.println("No students found.");
            } else {
                for (Student s : list) {
                    System.out.print("Student: " + s.getName() + " -> ");
                    if (s.getCourses().isEmpty()) {
                        System.out.println("[No Courses]");
                    } else {
                        s.getCourses().forEach(c -> System.out.print(c.getTitle() + " "));
                        System.out.println();
                    }
                }
            }
        } else if (choice == 3) {
            String name = readNameInput("Enter name to search: ");
            List<Student> result = enrollmentService.findStudentsByName(name);
            if (result.isEmpty()) {
                System.out.println("No students found.");
            } else {
                result.forEach(System.out::println);
            }
        } else if (choice == 4) {
            int minCredit = readPositiveInt("Enter minimum Credit: ");
            List<Course> result = enrollmentService.findCoursesByCreditGreaterThan(minCredit);
            if (result.isEmpty()) {
                System.out.println("No courses match criteria.");
            } else {
                result.forEach(System.out::println);
            }
        } else if (choice == 5) {
            List<Object[]> result = enrollmentService.getStudentCountPerCourse();
            if (result.isEmpty()) {
                System.out.println("No data available.");
            } else {
                result.forEach(row -> 
                    System.out.println(row[0] + ": " + row[1])
                );
            }
        }
    }

    private static void handleBonusQueries() {
        System.out.println("\n--- [6] Bonus Queries ---");
        System.out.println("1. Pagination: Get Students paginated");
        System.out.println("2. Find Students not enrolled in any course");
        System.out.print("Select option: ");
        int choice = readIntInput();

        if (choice == 1) {
            int page = readPositiveInt("Enter page number (1-based): ");
            int size = readPositiveInt("Enter page size: ");
            List<Student> result = enrollmentService.getAllStudentsPaginated(page, size);
            if (result.isEmpty()) {
                System.out.println("No data on this page.");
            } else {
                result.forEach(System.out::println);
            }
        } else if (choice == 2) {
            List<Student> result = enrollmentService.findStudentsWithNoCourses();
            if (result.isEmpty()) {
                System.out.println("All students are enrolled in at least one course.");
            } else {
                result.forEach(System.out::println);
            }
        }
    }
}
