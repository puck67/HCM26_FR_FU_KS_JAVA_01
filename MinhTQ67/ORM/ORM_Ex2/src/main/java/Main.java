import entity.Course;
import entity.Student;
import service.CourseService;
import service.StudentService;
import service.impl.CourseServiceImpl;
import service.impl.StudentServiceImpl;
import util.HibernateUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static final StudentService studentService = new StudentServiceImpl();
    private static final CourseService courseService = new CourseServiceImpl();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("==============================================");
        System.out.println("  TRAINING CENTER MANAGEMENT SYSTEM");
        System.out.println("==============================================");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = readInt("Select: ");
            switch (choice) {
                case 1:
                    studentMenu();
                    break;
                case 2:
                    courseMenu();
                    break;
                case 3:
                    enrollmentMenu();
                    break;
                case 4:
                    queryMenu();
                    break;
                case 5:
                    System.out.println("\nGoodbye!");
                    running = false;
                    break;
                default:
                    System.out.println("[!] Invalid option. Try again.");
                    break;
            }
        }

        HibernateUtil.shutdown();
        scanner.close();
    }

    // ===================== MAIN MENU =====================

    private static void printMainMenu() {
        System.out.println("\n============== MAIN MENU ==============");
        System.out.println("  1. Student Management");
        System.out.println("  2. Course Management");
        System.out.println("  3. Enrollment Management");
        System.out.println("  4. Queries and Reports");
        System.out.println("  5. Exit");
        System.out.println("=======================================");
    }

    // ===================== STUDENT MENU =====================

    private static void studentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------- Student Management -------");
            System.out.println("  1. Create a new student");
            System.out.println("  2. Update student information");
            System.out.println("  3. Delete a student");
            System.out.println("  4. View student by ID");
            System.out.println("  5. List all students");
            System.out.println("  6. Back to main menu");
            System.out.println("----------------------------------");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1: {
                    String name = readString("Enter name: ");
                    int age = readInt("Enter age: ");
                    studentService.createStudent(name, age);
                    System.out.println("[+] Student created successfully.");
                    break;
                }
                case 2: {
                    int id = readInt("Enter student ID: ");
                    String name = readString("Enter new name: ");
                    int age = readInt("Enter new age: ");
                    try {
                        studentService.updateStudent(id, name, age);
                        System.out.println("[+] Student updated.");
                    } catch (Exception e) {
                        System.out.println("[!] Error: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    int id = readInt("Enter student ID to delete: ");
                    studentService.deleteStudent(id);
                    System.out.println("[+] Student deleted.");
                    break;
                }
                case 4: {
                    int id = readInt("Enter student ID: ");
                    Student s = studentService.getStudentById(id);
                    if (s != null) printStudent(s);
                    else System.out.println("[!] Student not found.");
                    break;
                }
                case 5: {
                    List<Student> list = studentService.getAllStudents();
                    if (list.isEmpty()) System.out.println("[i] No students found.");
                    else list.forEach(Main::printStudent);
                    break;
                }
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("[!] Invalid option.");
                    break;
            }
        }
    }

    // ===================== COURSE MENU =====================

    private static void courseMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------- Course Management -------");
            System.out.println("  1. Create a new course");
            System.out.println("  2. Update course information");
            System.out.println("  3. Delete a course");
            System.out.println("  4. View course by ID");
            System.out.println("  5. List all courses");
            System.out.println("  6. Back to main menu");
            System.out.println("---------------------------------");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1: {
                    String title = readString("Enter course title: ");
                    int credit = readInt("Enter credits: ");
                    courseService.createCourse(title, credit);
                    System.out.println("[+] Course created successfully.");
                    break;
                }
                case 2: {
                    int id = readInt("Enter course ID: ");
                    String title = readString("Enter new title: ");
                    int credit = readInt("Enter new credits: ");
                    try {
                        courseService.updateCourse(id, title, credit);
                        System.out.println("[+] Course updated.");
                    } catch (Exception e) {
                        System.out.println("[!] Error: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    int id = readInt("Enter course ID to delete: ");
                    courseService.deleteCourse(id);
                    System.out.println("[+] Course deleted.");
                    break;
                }
                case 4: {
                    int id = readInt("Enter course ID: ");
                    Course c = courseService.getCourseById(id);
                    if (c != null) printCourse(c);
                    else System.out.println("[!] Course not found.");
                    break;
                }
                case 5: {
                    List<Course> list = courseService.getAllCourses();
                    if (list.isEmpty()) System.out.println("[i] No courses found.");
                    else list.forEach(Main::printCourse);
                    break;
                }
                case 6:
                    back = true;
                    break;
                default:
                    System.out.println("[!] Invalid option.");
                    break;
            }
        }
    }

    // ===================== ENROLLMENT MENU =====================

    private static void enrollmentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------- Enrollment Management -------");
            System.out.println("  1. Enroll a student in a course");
            System.out.println("  2. Remove a student from a course");
            System.out.println("  3. View courses of a student");
            System.out.println("  4. View students of a course");
            System.out.println("  5. Back to main menu");
            System.out.println("-------------------------------------");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1: {
                    int sid = readInt("Enter student ID: ");
                    int cid = readInt("Enter course ID: ");
                    try {
                        studentService.enrollStudentInCourse(sid, cid);
                        System.out.println("[+] Enrollment successful.");
                    } catch (Exception e) {
                        System.out.println("[!] Error: " + e.getMessage());
                    }
                    break;
                }
                case 2: {
                    int sid = readInt("Enter student ID: ");
                    int cid = readInt("Enter course ID: ");
                    try {
                        studentService.removeStudentFromCourse(sid, cid);
                        System.out.println("[+] Student removed from course.");
                    } catch (Exception e) {
                        System.out.println("[!] Error: " + e.getMessage());
                    }
                    break;
                }
                case 3: {
                    int sid = readInt("Enter student ID: ");
                    try {
                        List<Course> courses = studentService.getCoursesOfStudent(sid);
                        if (courses.isEmpty()) System.out.println("[i] No courses enrolled.");
                        else courses.forEach(Main::printCourse);
                    } catch (Exception e) {
                        System.out.println("[!] Error: " + e.getMessage());
                    }
                    break;
                }
                case 4: {
                    int cid = readInt("Enter course ID: ");
                    try {
                        List<Student> students = courseService.getStudentsOfCourse(cid);
                        if (students.isEmpty()) System.out.println("[i] No students enrolled.");
                        else students.forEach(Main::printStudent);
                    } catch (Exception e) {
                        System.out.println("[!] Error: " + e.getMessage());
                    }
                    break;
                }
                case 5:
                    back = true;
                    break;
                default:
                    System.out.println("[!] Invalid option.");
                    break;
            }
        }
    }

    // ===================== QUERY MENU =====================

    private static void queryMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n------- Queries and Reports -------");
            System.out.println("  1. Find students older than a given age (HQL)");
            System.out.println("  2. Find students by name (Named Query)");
            System.out.println("  3. List students and their courses (HQL Join)");
            System.out.println("  4. Find courses with credit greater than a value (Criteria API)");
            System.out.println("  5. Count number of students in each course (Aggregation)");
            System.out.println("  6. Find students enrolled in a specific course");
            System.out.println("  7. Back to main menu");
            System.out.println("-----------------------------------");

            int choice = readInt("Select: ");
            switch (choice) {
                case 1: {
                    int age = readInt("Enter minimum age: ");
                    List<Student> list = studentService.findStudentsOlderThan(age);
                    System.out.println("\n--- Students older than " + age + " ---");
                    if (list.isEmpty()) System.out.println("[i] No results.");
                    else list.forEach(Main::printStudent);
                    break;
                }
                case 2: {
                    String name = readString("Enter name (or partial name): ");
                    List<Student> list = studentService.findStudentsByName(name);
                    System.out.println("\n--- Students matching '" + name + "' ---");
                    if (list.isEmpty()) System.out.println("[i] No results.");
                    else list.forEach(Main::printStudent);
                    break;
                }
                case 3: {
                    List<Object[]> rows = studentService.findStudentsWithCourses();
                    System.out.println("\n--- Students and Their Courses ---");
                    if (rows.isEmpty()) System.out.println("[i] No enrollments found.");
                    else rows.forEach(r ->
                            System.out.printf("  Student: %-20s | Course: %s%n", r[0], r[1]));
                    break;
                }
                case 4: {
                    int credit = readInt("Enter minimum credit value: ");
                    List<Course> list = courseService.findCoursesWithCreditGreaterThan(credit);
                    System.out.println("\n--- Courses with credit > " + credit + " ---");
                    if (list.isEmpty()) System.out.println("[i] No results.");
                    else list.forEach(Main::printCourse);
                    break;
                }
                case 5: {
                    List<Object[]> rows = courseService.countStudentsPerCourse();
                    System.out.println("\n--- Student Count per Course ---");
                    if (rows.isEmpty()) System.out.println("[i] No courses found.");
                    else rows.forEach(r ->
                            System.out.printf("  Course: %-25s | Students enrolled: %s%n", r[0], r[1]));
                    break;
                }
                case 6: {
                    int cid = readInt("Enter course ID: ");
                    List<Student> list = studentService.findStudentsEnrolledInCourse(cid);
                    System.out.println("\n--- Students enrolled in course #" + cid + " ---");
                    if (list.isEmpty()) System.out.println("[i] No students found.");
                    else list.forEach(Main::printStudent);
                    break;
                }
                case 7:
                    back = true;
                    break;
                default:
                    System.out.println("[!] Invalid option.");
                    break;
            }
        }
    }

    // ===================== HELPERS =====================

    private static void printStudent(Student s) {
        System.out.printf("  [Student] ID: %d | Name: %-20s | Age: %d%n",
                s.getId(), s.getName(), s.getAge());
    }

    private static void printCourse(Course c) {
        System.out.printf("  [Course]  ID: %d | Title: %-25s | Credits: %d%n",
                c.getId(), c.getTitle(), c.getCredit());
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("[!] Input cannot be empty. Please enter a valid number.");
                continue;
            }
            try {
                int value = Integer.parseInt(input);
                if (value < 0) {
                    System.out.println("[!] Please enter a positive number.");
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number.");
            }
        }
    }

    private static String readString(String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            }
            System.out.println("[!] Input cannot be empty. Please try again.");
        }
    }
}
