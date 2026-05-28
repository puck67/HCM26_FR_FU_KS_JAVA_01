package fa.training.app;



import fa.training.dao.*;
import fa.training.dao.impl.*;
import fa.training.entity.*;
import fa.training.service.*;
import fa.training.service.impl.*;


import java.util.List;
import java.util.Scanner;

public class App {

    static Scanner sc = new Scanner(System.in);

    static StudentService studentService =
            new StudentServiceImpl();

    static CourseService courseService =
            new CourseServiceImpl();

    static StudentDAO studentDAO =
            new StudentDAOImpl();

    static CourseDAO courseDAO =
            new CourseDAOImpl();

    public static void main(String[] args) {

        while (true) {

            System.out.println("\n========== MAIN MENU ==========");

            System.out.println("1. Student Management");
            System.out.println("2. Course Management");
            System.out.println("3. Enrollment Management");
            System.out.println("4. Queries and Reports");
            System.out.println("5. Exit");

            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> studentMenu();

                case 2 -> courseMenu();

                case 3 -> enrollmentMenu();

                case 4 -> queryMenu();

                case 5 -> {

                    System.out.println("Goodbye!");

                    System.exit(0);
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // =========================
    // STUDENT MENU
    // =========================

    static void studentMenu() {

        while (true) {

            System.out.println("\n====== STUDENT MANAGEMENT ======");

            System.out.println("1. Create Student");
            System.out.println("2. Update Student");
            System.out.println("3. Delete Student");
            System.out.println("4. View Student By ID");
            System.out.println("5. List All Students");
            System.out.println("6. Back");

            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {

                    sc.nextLine();

                    System.out.print("Name: ");
                    String name = sc.nextLine();

                    System.out.print("Age: ");
                    int age = sc.nextInt();

                    studentService.createStudent(name, age);

                    System.out.println("Student created");
                }

                case 2 -> {

                    System.out.print("Student ID: ");
                    int id = sc.nextInt();

                    sc.nextLine();

                    System.out.print("New Name: ");
                    String name = sc.nextLine();

                    System.out.print("New Age: ");
                    int age = sc.nextInt();

                    studentService.updateStudent(id, name, age);

                    System.out.println("Student updated");
                }

                case 3 -> {

                    System.out.print("Student ID: ");

                    int id = sc.nextInt();

                    studentService.deleteStudent(id);

                    System.out.println("Student deleted");
                }

                case 4 -> {

                    System.out.print("Student ID: ");

                    int id = sc.nextInt();

                    Student s =
                            studentService.getStudentById(id);

                    if (s != null) {

                        System.out.println(s);

                    } else {

                        System.out.println("Student not found");
                    }
                }

                case 5 -> {

                    List<Student> students =
                            studentService.getAllStudents();

                    students.forEach(System.out::println);
                }

                case 6 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // =========================
    // COURSE MENU
    // =========================

    static void courseMenu() {

        while (true) {

            System.out.println("\n====== COURSE MANAGEMENT ======");

            System.out.println("1. Create Course");
            System.out.println("2. Update Course");
            System.out.println("3. Delete Course");
            System.out.println("4. View Course By ID");
            System.out.println("5. List All Courses");
            System.out.println("6. Back");

            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {

                    sc.nextLine();

                    System.out.print("Title: ");
                    String title = sc.nextLine();

                    System.out.print("Credit: ");
                    int credit = sc.nextInt();

                    courseService.createCourse(title, credit);

                    System.out.println("Course created");
                }

                case 2 -> {

                    System.out.print("Course ID: ");
                    int id = sc.nextInt();

                    sc.nextLine();

                    System.out.print("New Title: ");
                    String title = sc.nextLine();

                    System.out.print("New Credit: ");
                    int credit = sc.nextInt();

                    courseService.updateCourse(id, title, credit);

                    System.out.println("Course updated");
                }

                case 3 -> {

                    System.out.print("Course ID: ");

                    int id = sc.nextInt();

                    courseService.deleteCourse(id);

                    System.out.println("Course deleted");
                }

                case 4 -> {

                    System.out.print("Course ID: ");

                    int id = sc.nextInt();

                    Course c =
                            courseService.getCourseById(id);

                    if (c != null) {

                        System.out.println(c);

                    } else {

                        System.out.println("Course not found");
                    }
                }

                case 5 -> {

                    List<Course> courses =
                            courseService.getAllCourses();

                    courses.forEach(System.out::println);
                }

                case 6 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // =========================
    // ENROLLMENT MENU
    // =========================

    static void enrollmentMenu() {

        while (true) {

            System.out.println("\n====== ENROLLMENT MANAGEMENT ======");

            System.out.println("1. Enroll Student In Course");
            System.out.println("2. Remove Student From Course");
            System.out.println("3. View Courses Of Student");
            System.out.println("4. View Students Of Course");
            System.out.println("5. Back");

            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {

                    System.out.print("Student ID: ");
                    int sid = sc.nextInt();

                    System.out.print("Course ID: ");
                    int cid = sc.nextInt();

                    studentService
                            .enrollStudentInCourse(sid, cid);


                }

                case 2 -> {

                    System.out.print("Student ID: ");
                    int sid = sc.nextInt();

                    System.out.print("Course ID: ");
                    int cid = sc.nextInt();

                    studentService
                            .removeStudentFromCourse(sid, cid);

                    System.out.println("Removed successfully");
                }

                case 3 -> {

                    System.out.print("Student ID: ");

                    int sid = sc.nextInt();

                    studentService
                            .getCoursesOfStudent(sid)
                            .forEach(System.out::println);
                }

                case 4 -> {

                    System.out.print("Course ID: ");

                    int cid = sc.nextInt();

                    courseService
                            .getStudentsOfCourse(cid)
                            .forEach(System.out::println);
                }

                case 5 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }

    // =========================
    // QUERY MENU
    // =========================

    static void queryMenu() {

        while (true) {

            System.out.println("\n====== QUERIES & REPORTS ======");

            System.out.println("1. Find students older than age");
            System.out.println("2. Find students by name");
            System.out.println("3. List students and courses");
            System.out.println("4. Find courses with credit greater");
            System.out.println("5. Count students in each course");
            System.out.println("6. Find students by course");
            System.out.println("7. Back");

            System.out.print("Choose: ");

            int choice = sc.nextInt();

            switch (choice) {

                case 1 -> {

                    System.out.print("Age: ");

                    int age = sc.nextInt();

                    studentDAO
                            .findStudentsOlderThan(age)
                            .forEach(System.out::println);
                }

                case 2 -> {

                    sc.nextLine();

                    System.out.print("Name: ");

                    String name = sc.nextLine();

                    studentDAO
                            .findByName(name)
                            .forEach(System.out::println);
                }

                case 3 -> {

                    List<Object[]> list =
                            studentDAO.listStudentsAndCourses();

                    for (Object[] row : list) {

                        System.out.println(
                                row[0] + " studies " + row[1]
                        );
                    }
                }

                case 4 -> {

                    System.out.print("Credit: ");

                    int credit = sc.nextInt();

                    courseDAO
                            .findCoursesWithCreditGreaterThan(credit)
                            .forEach(System.out::println);
                }

                case 5 -> {

                    List<Object[]> list =
                            courseDAO.countStudentsInCourses();

                    for (Object[] row : list) {

                        System.out.println(
                                "Course: " + row[0]
                                        + " | Students: "
                                        + row[1]
                        );
                    }
                }

                case 6 -> {

                    System.out.print("Course ID: ");

                    int cid = sc.nextInt();

                    studentDAO
                            .findStudentsByCourse(cid)
                            .forEach(System.out::println);
                }

                case 7 -> {
                    return;
                }

                default -> System.out.println("Invalid choice");
            }
        }
    }
}