package fa.training;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.service.TrainingService;
import fa.training.util.HibernateUtil;

import java.util.*;
import java.util.logging.LogManager;

public class Main {

    static {
        LogManager.getLogManager().reset();
    }

    private static final TrainingService service = new TrainingService();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        insertSampleData();

        Map<Integer, Runnable> menuActions = new LinkedHashMap<>();
        menuActions.put(1, Main::studentMenu);
        menuActions.put(2, Main::courseMenu);
        menuActions.put(3, Main::enrollmentMenu);
        menuActions.put(4, Main::queryMenu);
        menuActions.put(0, () -> {
            System.out.println("Exiting...");
            HibernateUtil.shutdown();
            System.exit(0);
        });

        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n========== TRAINING CENTER MANAGEMENT ==========\n")
                    .append("1. Manage Students\n")
                    .append("2. Manage Courses\n")
                    .append("3. Enrollment\n")
                    .append("4. Queries\n")
                    .append("0. Exit\n")
                    .append("=================================================");
            System.out.println(sb.toString());

            int choice = readInt("Enter choice: ");
            Runnable action = menuActions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void studentMenu() {
        Map<Integer, Runnable> actions = new LinkedHashMap<>();
        actions.put(1, Main::addStudent);
        actions.put(2, Main::updateStudent);
        actions.put(3, Main::deleteStudent);
        actions.put(4, Main::getStudentById);
        actions.put(5, Main::getAllStudents);
        actions.put(0, () -> {});

        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Student Management ---\n")
                    .append("1. Add Student\n")
                    .append("2. Update Student\n")
                    .append("3. Delete Student\n")
                    .append("4. Get Student by ID\n")
                    .append("5. Get All Students\n")
                    .append("0. Back");
            System.out.println(sb.toString());

            int choice = readInt("Enter choice: ");
            if (choice == 0) break;
            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void addStudent() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        int age = readInt("Enter student age: ");
        Student student = new Student(name, age);
        service.createStudent(student);
        System.out.println("Created: " + student);
    }

    private static void updateStudent() {
        int id = readInt("Enter student ID to update: ");
        Student student = service.getStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        System.out.println("Current: " + student);
        System.out.print("Enter new name (press Enter to keep): ");
        String name = scanner.nextLine().trim();
        if (!name.isEmpty()) {
            student.setName(name);
        }
        System.out.print("Enter new age (0 to keep): ");
        int age = readInt("");
        if (age > 0) {
            student.setAge(age);
        }
        service.updateStudent(student);
        System.out.println("Updated: " + student);
    }

    private static void deleteStudent() {
        int id = readInt("Enter student ID to delete: ");
        Student student = service.getStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
            return;
        }
        service.deleteStudent(id);
        System.out.println("Deleted: " + student);
    }

    private static void getStudentById() {
        int id = readInt("Enter student ID: ");
        Student student = service.getStudentById(id);
        if (student == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println(student);
            Set<Course> courses = student.getCourses();
            if (!courses.isEmpty()) {
                System.out.println("Enrolled courses:");
                courses.forEach(c -> System.out.println("  - " + c.getTitle()));
            }
        }
    }

    private static void getAllStudents() {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        System.out.println("All students:");
        students.forEach(s -> System.out.println("  " + s));
    }

    private static void courseMenu() {
        Map<Integer, Runnable> actions = new LinkedHashMap<>();
        actions.put(1, Main::addCourse);
        actions.put(2, Main::updateCourse);
        actions.put(3, Main::deleteCourse);
        actions.put(4, Main::getAllCourses);
        actions.put(0, () -> {});

        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Course Management ---\n")
                    .append("1. Add Course\n")
                    .append("2. Update Course\n")
                    .append("3. Delete Course\n")
                    .append("4. Get All Courses\n")
                    .append("0. Back");
            System.out.println(sb.toString());

            int choice = readInt("Enter choice: ");
            if (choice == 0) break;
            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void addCourse() {
        System.out.print("Enter course title: ");
        String title = scanner.nextLine().trim();
        int credit = readInt("Enter course credit: ");
        Course course = new Course(title, credit);
        service.createCourse(course);
        System.out.println("Created: " + course);
    }

    private static void updateCourse() {
        int id = readInt("Enter course ID to update: ");
        Course course = service.getCourseById(id);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        System.out.println("Current: " + course);
        System.out.print("Enter new title (press Enter to keep): ");
        String title = scanner.nextLine().trim();
        if (!title.isEmpty()) {
            course.setTitle(title);
        }
        System.out.print("Enter new credit (0 to keep): ");
        int credit = readInt("");
        if (credit > 0) {
            course.setCredit(credit);
        }
        service.updateCourse(course);
        System.out.println("Updated: " + course);
    }

    private static void deleteCourse() {
        int id = readInt("Enter course ID to delete: ");
        Course course = service.getCourseById(id);
        if (course == null) {
            System.out.println("Course not found.");
            return;
        }
        service.deleteCourse(id);
        System.out.println("Deleted: " + course);
    }

    private static void getAllCourses() {
        List<Course> courses = service.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        System.out.println("All courses:");
        courses.forEach(c -> System.out.println("  " + c));
    }

    private static void enrollmentMenu() {
        Map<Integer, Runnable> actions = new LinkedHashMap<>();
        actions.put(1, Main::enrollStudent);
        actions.put(2, Main::removeEnrollment);
        actions.put(3, Main::showCoursesOfStudent);
        actions.put(4, Main::showStudentsOfCourse);
        actions.put(0, () -> {});

        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Enrollment Management ---\n")
                    .append("1. Enroll Student in Course\n")
                    .append("2. Remove Student from Course\n")
                    .append("3. Show Courses of a Student\n")
                    .append("4. Show Students of a Course\n")
                    .append("0. Back");
            System.out.println(sb.toString());

            int choice = readInt("Enter choice: ");
            if (choice == 0) break;
            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void enrollStudent() {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        service.enrollStudentInCourse(studentId, courseId);
        System.out.println("Enrolled successfully.");
    }

    private static void removeEnrollment() {
        int studentId = readInt("Enter student ID: ");
        int courseId = readInt("Enter course ID: ");
        service.removeStudentFromCourse(studentId, courseId);
        System.out.println("Removed successfully.");
    }

    private static void showCoursesOfStudent() {
        int studentId = readInt("Enter student ID: ");
        Set<Course> courses = service.getCoursesOfStudent(studentId);
        if (courses == null || courses.isEmpty()) {
            System.out.println("No courses found for this student.");
            return;
        }
        System.out.println("Courses:");
        courses.forEach(c -> System.out.println("  " + c));
    }

    private static void showStudentsOfCourse() {
        int courseId = readInt("Enter course ID: ");
        Set<Student> students = service.getStudentsOfCourse(courseId);
        if (students == null || students.isEmpty()) {
            System.out.println("No students found for this course.");
            return;
        }
        System.out.println("Students:");
        students.forEach(s -> System.out.println("  " + s));
    }

    private static void queryMenu() {
        Map<Integer, Runnable> actions = new LinkedHashMap<>();
        actions.put(1, Main::queryStudentsOlderThan);
        actions.put(2, Main::queryStudentsWithCourses);
        actions.put(3, Main::queryStudentByName);
        actions.put(4, Main::queryCoursesByCredit);
        actions.put(5, Main::queryStudentCountPerCourse);
        actions.put(6, Main::queryStudentsNotEnrolled);
        actions.put(7, Main::queryPagination);
        actions.put(0, () -> {});

        while (true) {
            StringBuilder sb = new StringBuilder();
            sb.append("\n--- Query Menu ---\n")
                    .append("1. [HQL] Students older than age\n")
                    .append("2. [HQL JOIN] Students with their courses\n")
                    .append("3. [Named Query] Find student by name\n")
                    .append("4. [Criteria API] Courses by credit >\n")
                    .append("5. [Aggregation] Student count per course\n")
                    .append("6. Students not enrolled in any course\n")
                    .append("7. Pagination - list all students\n")
                    .append("0. Back");
            System.out.println(sb.toString());

            int choice = readInt("Enter choice: ");
            if (choice == 0) break;
            Runnable action = actions.get(choice);
            if (action != null) {
                action.run();
            } else {
                System.out.println("Invalid choice.");
            }
        }
    }

    private static void queryStudentsOlderThan() {
        int age = readInt("Enter age: ");
        List<Student> students = service.findStudentsOlderThan(age);
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Students older than ").append(age).append(":");
        System.out.println(sb.toString());
        students.forEach(s -> System.out.println("  " + s));
    }

    private static void queryStudentsWithCourses() {
        List<Object[]> results = service.findStudentsWithCourses();
        if (results.isEmpty()) {
            System.out.println("No data found.");
            return;
        }
        System.out.println("Students and their courses:");
        results.forEach(row -> {
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(row[0]).append(" -> ").append(row[1]);
            System.out.println(sb.toString());
        });
    }

    private static void queryStudentByName() {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine().trim();
        List<Student> students = service.findStudentsByName(name);
        if (students.isEmpty()) {
            System.out.println("No students found with name: " + name);
            return;
        }
        students.forEach(s -> System.out.println("  " + s));
    }

    private static void queryCoursesByCredit() {
        int credit = readInt("Enter minimum credit: ");
        List<Course> courses = service.findCoursesByCreditGreaterThan(credit);
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Courses with credit > ").append(credit).append(":");
        System.out.println(sb.toString());
        courses.forEach(c -> System.out.println("  " + c));
    }

    private static void queryStudentCountPerCourse() {
        List<Object[]> results = service.countStudentsPerCourse();
        System.out.println("Student count per course:");
        results.forEach(row -> {
            StringBuilder sb = new StringBuilder();
            sb.append("  ").append(row[0]).append(": ").append(row[1]);
            System.out.println(sb.toString());
        });
    }

    private static void queryStudentsNotEnrolled() {
        List<Student> students = service.findStudentsNotEnrolled();
        if (students.isEmpty()) {
            System.out.println("All students are enrolled in at least one course.");
            return;
        }
        System.out.println("Students not enrolled:");
        students.forEach(s -> System.out.println("  " + s));
    }

    private static void queryPagination() {
        int pageSize = readInt("Enter page size: ");
        if (pageSize <= 0) pageSize = 2;
        List<Student> all = service.getAllStudents();
        int totalPages = (int) Math.ceil((double) all.size() / pageSize);

        for (int page = 1; page <= totalPages; page++) {
            StringBuilder sb = new StringBuilder();
            sb.append("\nPage ").append(page).append("/").append(totalPages).append(":");
            System.out.println(sb.toString());
            service.getAllStudents(page, pageSize)
                    .forEach(s -> System.out.println("  " + s));
        }
    }

    private static void insertSampleData() {
        if (!service.getAllStudents().isEmpty()) {
            System.out.println("=== Sample data already exists, skipping insert ===");
            System.out.println();
            return;
        }

        System.out.println("=== Inserting Sample Data ===");

        Student s1 = new Student("John", 19);
        Student s2 = new Student("Anna", 22);
        Student s3 = new Student("Peter", 25);
        Student s4 = new Student("Mary", 18);

        service.createStudent(s1);
        service.createStudent(s2);
        service.createStudent(s3);
        service.createStudent(s4);

        Course c1 = new Course("Java Programming", 3);
        Course c2 = new Course("Database Systems", 4);
        Course c3 = new Course("Web Development", 3);
        Course c4 = new Course("Data Structures", 2);

        service.createCourse(c1);
        service.createCourse(c2);
        service.createCourse(c3);
        service.createCourse(c4);

        service.enrollStudentInCourse(s1.getId(), c1.getId());
        service.enrollStudentInCourse(s1.getId(), c2.getId());
        service.enrollStudentInCourse(s2.getId(), c1.getId());
        service.enrollStudentInCourse(s2.getId(), c2.getId());
        service.enrollStudentInCourse(s2.getId(), c3.getId());
        service.enrollStudentInCourse(s3.getId(), c2.getId());
        service.enrollStudentInCourse(s3.getId(), c3.getId());

        System.out.println("Sample data inserted successfully!");
        System.out.println("Students: John(19), Anna(22), Peter(25), Mary(18)");
        System.out.println("Courses: Java Programming(3), Database Systems(4), Web Development(3), Data Structures(2)");
        System.out.println();
    }

    private static int readInt(String prompt) {
        if (!prompt.isEmpty()) System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            scanner.next();
            System.out.print("Invalid input. Enter a number: ");
        }
        int val = scanner.nextInt();
        scanner.nextLine();
        return val;
    }
}
