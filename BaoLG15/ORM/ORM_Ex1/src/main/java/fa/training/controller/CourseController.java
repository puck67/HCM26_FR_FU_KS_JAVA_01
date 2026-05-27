package fa.training.controller;

import fa.training.dao.CourseDAO;
import fa.training.entities.Course;
import fa.training.util.ValidationUtil;
import fa.training.view.ConsoleView;
import java.util.List;

public class CourseController {
    private final CourseDAO courseDAO;
    private final ConsoleView view;

    public CourseController(CourseDAO courseDAO, ConsoleView view) {
        this.courseDAO = courseDAO;
        this.view = view;
    }

    public void handleMenu() {
        boolean back = false;
        view.printCourseMenu();
        while (!back) {
            int choice = view.readInt("Enter your choice (1-8): ");
            System.out.println();
            switch (choice) {
                case 1 -> {
                    addCourse();
                    view.printCourseMenu();
                }
                case 2 -> {
                    updateCourse();
                    view.printCourseMenu();
                }
                case 3 -> {
                    deleteCourse();
                    view.printCourseMenu();
                }
                case 4 -> {
                    findCourseById();
                    view.printCourseMenu();
                }
                case 5 -> {
                    listAllCourses();
                    view.printCourseMenu();
                }
                case 6 -> {
                    findCoursesWithCreditsGreaterThan();
                    view.printCourseMenu();
                }
                case 7 -> {
                    countStudentsPerCourse();
                    view.printCourseMenu();
                }
                case 8 -> back = true;
                default -> view.displayMessage("Invalid option. Please enter a choice between 1 and 8.");
            }
            System.out.println();
        }
    }

    private void addCourse() {
        view.displayMessage("--- Add Course ---");
        String title = view.readString("Enter Course Title: ");
        if (!ValidationUtil.validateCourseTitle(title)) {
            return;
        }
        int credit = view.readInt("Enter Course Credit: ");
        if (!ValidationUtil.validateCourseCredit(credit)) {
            return;
        }
        Course course = new Course(title, credit);
        courseDAO.save(course);
        view.displayMessage("Course saved successfully: " + course);
    }

    private void updateCourse() {
        view.displayMessage("--- Update Course ---");
        int id = view.readInt("Enter Course ID to update: ");
        Course course = courseDAO.findById(id);
        if (course == null) {
            view.displayMessage("Course with ID " + id + " not found.");
            return;
        }
        view.displayMessage("Current Data: " + course);
        String title = view.readString("Enter New Title: ");
        if (!ValidationUtil.validateCourseTitle(title)) {
            return;
        }
        int credit = view.readInt("Enter New Credit: ");
        if (!ValidationUtil.validateCourseCredit(credit)) {
            return;
        }
        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
        view.displayMessage("Course updated successfully.");
    }

    private void deleteCourse() {
        view.displayMessage("--- Delete Course ---");
        int id = view.readInt("Enter Course ID to delete: ");
        Course course = courseDAO.findById(id);
        if (course == null) {
            view.displayMessage("Course with ID " + id + " not found.");
            return;
        }
        courseDAO.delete(id);
        view.displayMessage("Course deleted successfully.");
    }

    private void findCourseById() {
        view.displayMessage("--- Find Course by ID ---");
        int id = view.readInt("Enter Course ID: ");
        Course course = courseDAO.findById(id);
        view.displayCourse(course);
    }

    private void listAllCourses() {
        view.displayMessage("--- All Courses ---");
        List<Course> courses = courseDAO.findAll();
        view.displayCourses(courses);
    }

    private void findCoursesWithCreditsGreaterThan() {
        view.displayMessage("--- Find Courses by Credit Threshold (Criteria API) ---");
        int credit = view.readInt("Enter minimum credit threshold (exclusive): ");
        List<Course> courses = courseDAO.findCoursesWithCreditGreaterThan(credit);
        view.displayCourses(courses);
    }

    private void countStudentsPerCourse() {
        view.displayMessage("--- Student Count per Course (Aggregation Query) ---");
        List<Object[]> counts = courseDAO.countStudentsPerCourse();
        view.displayStudentCounts(counts);
    }
}
