package fa.training.app;

import fa.training.controller.CourseController;
import fa.training.controller.EnrollmentController;
import fa.training.controller.QueryController;
import fa.training.controller.StudentController;
import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.ConsoleUtil;
import fa.training.util.HibernateUtil;
import fa.training.view.MainView;
import org.slf4j.bridge.SLF4JBridgeHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.LogManager;

public class Main {

    private static final MainView view = new MainView();
    private static final StudentController studentController = new StudentController();
    private static final CourseController courseController = new CourseController();
    private static final EnrollmentController enrollmentController = new EnrollmentController();
    private static final QueryController queryController = new QueryController();

    public static void main(String[] args) {
        // Route java.util.logging -> SLF4J so Hibernate INFO messages are silenced
        LogManager.getLogManager().reset();
        SLF4JBridgeHandler.install();

        // Insert sample data
        seedSampleData();

        // --- Main menu ---
        Map<Integer, Runnable> mainActions = new HashMap<>();
        mainActions.put(1, Main::studentMenu);
        mainActions.put(2, Main::courseMenu);
        mainActions.put(3, Main::enrollmentMenu);
        mainActions.put(4, Main::queryMenu);

        boolean running = true;

        while (running) {
            view.printMainMenu();
            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 5) {
                running = false;
                HibernateUtil.shutdown();
                view.printExitMessage();
                continue;
            }

            mainActions.getOrDefault(
                    choice,
                    view::printInvalidChoice).run();
        }
    }

    // ── Sample data ───────────────────────────────────

    private static void seedSampleData() {

        StudentDAO studentDAO = new StudentDAOImpl();
        CourseDAO courseDAO = new CourseDAOImpl();

        // Avoid duplicate inserts every run
        if (!studentDAO.getAll().isEmpty()
                || !courseDAO.getAll().isEmpty()) {
            return;
        }

        // Students
        Student s1 = new Student("John Doe", 20);
        Student s2 = new Student("Jane Smith", 21);
        Student s3 = new Student("Michael Brown", 22);

        studentDAO.add(s1);
        studentDAO.add(s2);
        studentDAO.add(s3);

        // Courses
        Course c1 = new Course("Java Programming", 3);
        Course c2 = new Course("Database Systems", 4);
        Course c3 = new Course("Hibernate ORM", 3);

        courseDAO.add(c1);
        courseDAO.add(c2);
        courseDAO.add(c3);

        System.out.println("Sample data inserted!");
    }

    // --- Student sub-menu ---
    private static void studentMenu() {
        Map<Integer, Runnable> actions = Map.of(
                1, studentController::addStudent,
                2, studentController::updateStudent,
                3, studentController::deleteStudent,
                4, studentController::findStudentById,
                5, studentController::displayAllStudents);

        boolean back = false;
        while (!back) {
            studentController.getView().printMenu();
            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 6) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    studentController.getView()::printInvalidChoice).run();
        }
    }

    // --- Course sub-menu ---
    private static void courseMenu() {
        Map<Integer, Runnable> actions = Map.of(
                1, courseController::addCourse,
                2, courseController::updateCourse,
                3, courseController::deleteCourse,
                4, courseController::findCourseById,
                5, courseController::displayAllCourses);

        boolean back = false;
        while (!back) {
            courseController.getView().printMenu();
            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 6) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    courseController.getView()::printInvalidChoice).run();
        }
    }

    // --- Enrollment sub-menu ---
    private static void enrollmentMenu() {
        Map<Integer, Runnable> actions = Map.of(
                1, enrollmentController::enrollStudent,
                2, enrollmentController::unenrollStudent,
                3, enrollmentController::viewCoursesOfStudent,
                4, enrollmentController::viewStudentsOfCourse);

        boolean back = false;
        while (!back) {
            enrollmentController.getView().printMenu();
            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 5) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    enrollmentController.getView()::printInvalidChoice).run();
        }
    }

    // --- Query sub-menu ---
    private static void queryMenu() {
        Map<Integer, Runnable> actions = Map.of(
                1, queryController::findStudentsOlderThan,
                2, queryController::findStudentsByName,
                3, queryController::listStudentsWithCourses,
                4, queryController::findCoursesWithHighCredit,
                5, queryController::countStudentsPerCourse,
                6, queryController::findStudentsEnrolledInCourse);

        boolean back = false;
        while (!back) {
            queryController.getView().printMenu();
            int choice = ConsoleUtil.readInt("Choose: ");

            if (choice == 7) {
                back = true;
                continue;
            }

            actions.getOrDefault(
                    choice,
                    queryController.getView()::printInvalidChoice).run();
        }
    }
}
