import entity.Course;
import entity.Student;
import service.CourseService;
import service.StudentService;
import service.impl.CourseServiceImpl;
import service.impl.StudentServiceImpl;
import util.HibernateUtil;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();

    public static void main(String[] args) {
        try {
            boolean running = true;
            while (running) {
                printMainMenu();
                String choice = sc.nextLine().trim();
                switch (choice) {
                    case "1":
                        studentMenu();
                        break;
                    case "2":
                        courseMenu();
                        break;
                    case "3":
                        enrollmentMenu();
                        break;
                    case "4":
                        queryMenu();
                        break;
                    case "5":
                        System.out.println("\nExiting program... Goodbye!");
                        running = false;
                        break;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 1 and 5.");
                }
            }
        } finally {
            HibernateUtil.shutdown();
        }
    }

    // ===================== MAIN MENU =====================
    private static void printMainMenu() {
        System.out.println("\n=============================================");
        System.out.println("      TRAINING CENTER MANAGEMENT SYSTEM      ");
        System.out.println("=============================================");
        System.out.println("1. Student Management");
        System.out.println("2. Course Management");
        System.out.println("3. Enrollment Management");
        System.out.println("4. Queries and Reports");
        System.out.println("5. Exit");
        System.out.println("=============================================");
        System.out.print("Your choice: ");
    }

    // ===================== STUDENT MANAGEMENT MENU =====================
    private static void studentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Student Management ---");
            System.out.println("1. Create a new student");
            System.out.println("2. Update student information");
            System.out.println("3. Delete a student");
            System.out.println("4. View student by ID");
            System.out.println("5. List all students");
            System.out.println("6. Back to main menu");
            System.out.print("Your choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    createStudent();
                    break;
                case "2":
                    updateStudent();
                    break;
                case "3":
                    deleteStudent();
                    break;
                case "4":
                    viewStudentById();
                    break;
                case "5":
                    listAllStudents();
                    break;
                case "6":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createStudent() {
        try {
            System.out.print("Enter name: ");
            String name = sc.nextLine().trim();
            int age = inputPositiveInt("Enter age: ");
            studentService.createStudent(name, age);
            System.out.println("Student added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateStudent() {
        try {
            int id = inputPositiveInt("Enter Student ID to update: ");
            Student s = studentService.getStudentById(id);
            if (s == null) {
                System.out.println("Student not found.");
                return;
            }
            System.out.println("Current info: " + s);
            System.out.print("Enter new name: ");
            String name = sc.nextLine().trim();
            int age = inputPositiveInt("Enter new age: ");
            studentService.updateStudent(id, name, age);
            System.out.println("Student updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteStudent() {
        try {
            int id = inputPositiveInt("Enter Student ID to delete: ");
            studentService.deleteStudent(id);
            System.out.println("Student deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewStudentById() {
        int id = inputPositiveInt("Enter Student ID: ");
        Student s = studentService.getStudentById(id);
        if (s == null) {
            System.out.println("Student not found.");
        } else {
            System.out.println("Found: " + s);
        }
    }

    private static void listAllStudents() {
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
        } else {
            System.out.println("Students List:");
            students.forEach(s -> System.out.println("  ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    // ===================== COURSE MANAGEMENT MENU =====================
    private static void courseMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Course Management ---");
            System.out.println("1. Create a new course");
            System.out.println("2. Update course information");
            System.out.println("3. Delete a course");
            System.out.println("4. View course by ID");
            System.out.println("5. List all courses");
            System.out.println("6. Back to main menu");
            System.out.print("Your choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    createCourse();
                    break;
                case "2":
                    updateCourse();
                    break;
                case "3":
                    deleteCourse();
                    break;
                case "4":
                    viewCourseById();
                    break;
                case "5":
                    listAllCourses();
                    break;
                case "6":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void createCourse() {
        try {
            System.out.print("Enter course title: ");
            String title = sc.nextLine().trim();
            int credit = inputPositiveInt("Enter course credit: ");
            courseService.createCourse(title, credit);
            System.out.println("Course added successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void updateCourse() {
        try {
            int id = inputPositiveInt("Enter Course ID to update: ");
            Course c = courseService.getCourseById(id);
            if (c == null) {
                System.out.println("Course not found.");
                return;
            }
            System.out.println("Current info: " + c);
            System.out.print("Enter new title: ");
            String title = sc.nextLine().trim();
            int credit = inputPositiveInt("Enter new credit: ");
            courseService.updateCourse(id, title, credit);
            System.out.println("Course updated successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void deleteCourse() {
        try {
            int id = inputPositiveInt("Enter Course ID to delete: ");
            courseService.deleteCourse(id);
            System.out.println("Course deleted successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewCourseById() {
        int id = inputPositiveInt("Enter Course ID: ");
        Course c = courseService.getCourseById(id);
        if (c == null) {
            System.out.println("Course not found.");
        } else {
            System.out.println("Found: " + c);
        }
    }

    private static void listAllCourses() {
        List<Course> courses = courseService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses found.");
        } else {
            System.out.println("Courses List:");
            courses.forEach(c -> System.out.println("  ID: " + c.getId() + " | Title: " + c.getTitle() + " | Credit: " + c.getCredit()));
        }
    }

    // ===================== ENROLLMENT MANAGEMENT MENU =====================
    private static void enrollmentMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Enrollment Management ---");
            System.out.println("1. Enroll student in course");
            System.out.println("2. Remove student from course");
            System.out.println("3. View courses of a student");
            System.out.println("4. View students of a course");
            System.out.println("5. Back to main menu");
            System.out.print("Your choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    enrollStudent();
                    break;
                case "2":
                    removeStudentFromCourse();
                    break;
                case "3":
                    viewCoursesOfStudent();
                    break;
                case "4":
                    viewStudentsOfCourse();
                    break;
                case "5":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void enrollStudent() {
        try {
            int sId = inputPositiveInt("Enter Student ID: ");
            int cId = inputPositiveInt("Enter Course ID: ");
            studentService.enrollStudentInCourse(sId, cId);
            System.out.println("Enrolled student in course successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void removeStudentFromCourse() {
        try {
            int sId = inputPositiveInt("Enter Student ID: ");
            int cId = inputPositiveInt("Enter Course ID: ");
            studentService.removeStudentFromCourse(sId, cId);
            System.out.println("Removed student from course successfully!");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewCoursesOfStudent() {
        try {
            int sId = inputPositiveInt("Enter Student ID: ");
            Set<Course> courses = studentService.getCoursesOfStudent(sId);
            if (courses.isEmpty()) {
                System.out.println("This student is not enrolled in any courses.");
            } else {
                System.out.println("Courses for Student ID " + sId + ":");
                courses.forEach(c -> System.out.println("  - ID: " + c.getId() + " | Title: " + c.getTitle() + " (Credit: " + c.getCredit() + ")"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void viewStudentsOfCourse() {
        try {
            int cId = inputPositiveInt("Enter Course ID: ");
            Set<Student> students = courseService.getStudentsOfCourse(cId);
            if (students.isEmpty()) {
                System.out.println("No students enrolled in this course.");
            } else {
                System.out.println("Students enrolled in Course ID " + cId + ":");
                students.forEach(s -> System.out.println("  - ID: " + s.getId() + " | Name: " + s.getName() + " (Age: " + s.getAge() + ")"));
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    // ===================== QUERIES AND REPORTS MENU =====================
    private static void queryMenu() {
        boolean inMenu = true;
        while (inMenu) {
            System.out.println("\n--- Queries and Reports ---");
            System.out.println("1. Find students older than a given age (HQL)");
            System.out.println("2. Find students by name (Named Query)");
            System.out.println("3. List students and their courses (HQL Join)");
            System.out.println("4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("5. Count number of students in each course (Aggregation Query)");
            System.out.println("6. Find students enrolled in a specific course (Parameterized Query)");
            System.out.println("7. Back to main menu");
            System.out.print("Your choice: ");
            String choice = sc.nextLine().trim();
            switch (choice) {
                case "1":
                    findOlderThan();
                    break;
                case "2":
                    findByName();
                    break;
                case "3":
                    listStudentsWithCourses();
                    break;
                case "4":
                    findCoursesWithCreditGreaterThan();
                    break;
                case "5":
                    countStudentsPerCourse();
                    break;
                case "6":
                    findStudentsEnrolledInCourse();
                    break;
                case "7":
                    inMenu = false;
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void findOlderThan() {
        int age = inputPositiveInt("Enter minimum age: ");
        List<Student> students = studentService.findStudentsOlderThan(age);
        if (students.isEmpty()) {
            System.out.println("No students older than " + age + " found.");
        } else {
            System.out.println("Students older than " + age + ":");
            students.forEach(s -> System.out.println("  - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    private static void findByName() {
        System.out.print("Enter name to search: ");
        String name = sc.nextLine().trim();
        List<Student> students = studentService.findStudentsByName(name);
        if (students.isEmpty()) {
            System.out.println("No students found with name '" + name + "'.");
        } else {
            System.out.println("Found students:");
            students.forEach(s -> System.out.println("  - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    private static void listStudentsWithCourses() {
        List<Object[]> results = studentService.findStudentsWithCourses();
        if (results.isEmpty()) {
            System.out.println("No student enrollment data available.");
        } else {
            System.out.println("Students and their courses:");
            results.forEach(row -> System.out.println("  Student Name: " + row[0] + " | Course Title: " + row[1]));
        }
    }

    private static void findCoursesWithCreditGreaterThan() {
        int minCredit = inputPositiveInt("Enter minimum credit: ");
        List<Course> courses = courseService.findCoursesWithCreditGreaterThan(minCredit);
        if (courses.isEmpty()) {
            System.out.println("No courses with credit > " + minCredit + " found.");
        } else {
            System.out.println("Courses with credit > " + minCredit + ":");
            courses.forEach(c -> System.out.println("  - ID: " + c.getId() + " | Title: " + c.getTitle() + " | Credit: " + c.getCredit()));
        }
    }

    private static void countStudentsPerCourse() {
        List<Object[]> results = studentService.countStudentsPerCourse();
        if (results.isEmpty()) {
            System.out.println("No student enrollment data available.");
        } else {
            System.out.println("Student count per course:");
            results.forEach(row -> System.out.println("  Course: " + row[0] + " | Students enrolled: " + row[1]));
        }
    }

    private static void findStudentsEnrolledInCourse() {
        int cId = inputPositiveInt("Enter Course ID: ");
        List<Student> students = studentService.findStudentsEnrolledInCourse(cId);
        if (students.isEmpty()) {
            System.out.println("No students enrolled in this course or course not found.");
        } else {
            System.out.println("Students enrolled in Course ID " + cId + ":");
            students.forEach(s -> System.out.println("  - ID: " + s.getId() + " | Name: " + s.getName() + " | Age: " + s.getAge()));
        }
    }

    // ===================== HELPER =====================
    private static int inputPositiveInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            try {
                int val = Integer.parseInt(input);
                if (val <= 0) {
                    System.out.println("Please enter a positive integer.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer.");
            }
        }
    }
}
