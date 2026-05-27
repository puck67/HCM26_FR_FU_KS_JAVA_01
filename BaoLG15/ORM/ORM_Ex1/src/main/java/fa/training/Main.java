package fa.training;

import fa.training.controller.CourseController;
import fa.training.controller.EnrollmentController;
import fa.training.controller.StudentController;
import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.util.HibernateUtil;
import fa.training.view.ConsoleView;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    public static void main(String[] args) {
        silenceLogging();

        StudentDAO studentDAO = new StudentDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();
        Scanner scanner = new Scanner(System.in);
        ConsoleView view = new ConsoleView(scanner);

        StudentController studentController = new StudentController(studentDAO, view);
        CourseController courseController = new CourseController(courseDAO, view);
        EnrollmentController enrollmentController = new EnrollmentController(studentDAO, courseDAO, view);

        System.out.println("==================================================");
        System.out.println("          WELCOME TO THE TRAINING CENTER          ");
        System.out.println("==================================================");

        boolean running = true;
        view.printMainMenu();
        while (running) {
            int choice = view.readInt("Enter your choice (1-6): ");
            System.out.println();
            switch (choice) {
                case 1 -> {
                    studentController.handleMenu();
                    view.printMainMenu();
                }
                case 2 -> {
                    courseController.handleMenu();
                    view.printMainMenu();
                }
                case 3 -> {
                    enrollmentController.handleMenu();
                    view.printMainMenu();
                }
                case 4 -> {
                    seedSampleData(studentDAO, courseDAO, view);
                    view.printMainMenu();
                }
                case 5 -> {
                    runTask5Queries(studentDAO, courseDAO, view);
                    view.printMainMenu();
                }
                case 6 -> running = false;
                default -> view.displayMessage("Invalid option. Please enter a choice between 1 and 6.");
            }
            System.out.println();
        }

        HibernateUtil.shutdown();
        scanner.close();
        view.displayMessage("SessionFactory closed. Goodbye!");
    }

    private static void silenceLogging() {
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.SEVERE);
        for (Handler handler : rootLogger.getHandlers()) {
            handler.setLevel(Level.SEVERE);
        }
        Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
        Logger.getLogger("org.jboss.logging").setLevel(Level.SEVERE);
    }

    private static void seedSampleData(StudentDAO studentDAO, CourseDAO courseDAO, ConsoleView view) {
        if (!studentDAO.findAll().isEmpty() || !courseDAO.findAll().isEmpty()) {
            view.displayMessage("Database already contains data. Seeding is skipped to avoid duplicates.");
            return;
        }

        Course java = new Course("Java Programming", 4);
        Course db = new Course("Database Systems", 3);
        Course web = new Course("Web Development", 2);

        courseDAO.save(java);
        courseDAO.save(db);
        courseDAO.save(web);

        Student john = new Student("John", 21);
        Student anna = new Student("Anna", 22);
        Student david = new Student("David", 19);
        Student bob = new Student("Bob", 20);

        studentDAO.save(john);
        studentDAO.save(anna);
        studentDAO.save(david);
        studentDAO.save(bob);

        studentDAO.enrollStudentInCourse(john.getId(), java.getId());
        studentDAO.enrollStudentInCourse(john.getId(), db.getId());
        studentDAO.enrollStudentInCourse(anna.getId(), java.getId());
        studentDAO.enrollStudentInCourse(anna.getId(), db.getId());
        studentDAO.enrollStudentInCourse(anna.getId(), web.getId());
        studentDAO.enrollStudentInCourse(david.getId(), web.getId());

        view.displayMessage("Sample data seeded successfully.");
    }

    private static void runTask5Queries(StudentDAO studentDAO, CourseDAO courseDAO, ConsoleView view) {
        System.out.println("==================================================");
        System.out.println("Executing Task 5 Queries...");
        System.out.println("==================================================");

        System.out.println("- Students older than 20:");
        List<Student> olderStudents = studentDAO.findStudentsOlderThan(20);
        if (olderStudents.isEmpty()) {
            System.out.println("None");
        } else {
            olderStudents.forEach(System.out::println);
        }

        System.out.println("- Courses of student John:");
        List<Student> johns = studentDAO.findByName("John");
        if (!johns.isEmpty()) {
            int johnId = johns.get(0).getId();
            List<Course> johnCourses = studentDAO.getCoursesByStudent(johnId);
            if (johnCourses.isEmpty()) {
                System.out.println("No enrolled courses");
            } else {
                johnCourses.forEach(c -> System.out.println(c.getTitle()));
            }
        } else {
            System.out.println("Student John not found.");
        }

        System.out.println("- Student count per course:");
        List<Object[]> studentCounts = courseDAO.countStudentsPerCourse();
        if (studentCounts.isEmpty()) {
            System.out.println("No courses.");
        } else {
            studentCounts.forEach(row -> System.out.println(row[0] + ": " + row[1]));
        }

        System.out.println("- Find students by name 'Anna' (Named Query):");
        List<Student> annas = studentDAO.findByName("Anna");
        if (annas.isEmpty()) {
            System.out.println("None");
        } else {
            annas.forEach(System.out::println);
        }

        System.out.println("- Find courses with credit > 2 (Criteria API):");
        List<Course> creditCourses = courseDAO.findCoursesWithCreditGreaterThan(2);
        if (creditCourses.isEmpty()) {
            System.out.println("None");
        } else {
            creditCourses.forEach(System.out::println);
        }

        System.out.println("- List students and their courses (HQL Join):");
        List<Object[]> enrollments = studentDAO.listStudentsAndCourses();
        if (enrollments.isEmpty()) {
            System.out.println("No enrollments.");
        } else {
            enrollments.forEach(pair -> System.out.println("Student: " + pair[0] + ", Course: " + pair[1]));
        }

        System.out.println("- Students not enrolled in any course:");
        List<Student> unenrolled = studentDAO.findStudentsNotEnrolled();
        if (unenrolled.isEmpty()) {
            System.out.println("None");
        } else {
            unenrolled.forEach(System.out::println);
        }
        System.out.println("==================================================");
    }
}