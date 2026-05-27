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
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
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

        seedSampleData(studentDAO, courseDAO);

        boolean running = true;
        view.printMainMenu();
        while (running) {
            int choice = view.readInt("Enter your choice (1-5): ");
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
                    seedSampleData(studentDAO, courseDAO);
                    view.displayMessage("Sample data seeded successfully.");
                    view.printMainMenu();
                }
                case 5 -> running = false;
                default -> view.displayMessage("Invalid option. Please enter a choice between 1 and 5.");
            }
            System.out.println();
        }

        HibernateUtil.shutdown();
        scanner.close();
        view.displayMessage("SessionFactory closed. Goodbye!");
    }

    private static void seedSampleData(StudentDAO studentDAO, CourseDAO courseDAO) {
        if (!studentDAO.findAll().isEmpty() || !courseDAO.findAll().isEmpty()) {
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
    }
}