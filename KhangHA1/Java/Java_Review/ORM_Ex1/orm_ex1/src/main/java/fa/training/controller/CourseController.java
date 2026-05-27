package fa.training.controller;

import fa.training.dao.CourseDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.entity.Course;
import fa.training.util.ConsoleUtil;
import fa.training.view.CourseView;

import java.util.Scanner;

/**
 * Controller for Course CRUD — mirrors the LibraryManagement pattern.
 */
public class CourseController {

    private final CourseDAO  courseDAO = new CourseDAOImpl();
    private final CourseView view      = new CourseView();
    private final Scanner    scanner   = new Scanner(System.in);

    public CourseView getView() { return view; }

    // ------------------------------------------------------------------ //
    //  Add
    // ------------------------------------------------------------------ //

    public void addCourse() {
        view.printHeader("Add New Course");

        String title = ConsoleUtil.readString("Enter course title: ");

        int credit;
        while (true) {
            credit = ConsoleUtil.readInt("Enter credits: ");
            if (credit > 0) break;
            view.printWarning("Credits must be greater than 0.");
        }

        Course course = new Course(title, credit);

        if (courseDAO.add(course)) {
            view.printSuccess("Course added successfully (id=" + course.getId() + ").");
        } else {
            view.printError("Failed to add course.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Display all
    // ------------------------------------------------------------------ //

    public void displayAllCourses() {
        view.displayCourses(courseDAO.getAll());
    }

    // ------------------------------------------------------------------ //
    //  Find by ID
    // ------------------------------------------------------------------ //

    public void findCourseById() {
        view.printHeader("Find Course by ID");
        int id = ConsoleUtil.readInt("Enter course ID: ");
        view.displayCourse(courseDAO.findById(id));
    }

    // ------------------------------------------------------------------ //
    //  Update
    // ------------------------------------------------------------------ //

    public void updateCourse() {
        view.printHeader("Update Course");

        int id = ConsoleUtil.readInt("Enter course ID: ");
        Course existing = courseDAO.findById(id);

        if (existing == null) {
            view.printWarning("Course not found.");
            return;
        }

        System.out.print("Enter new title [" + existing.getTitle() + "]: ");
        String title = scanner.nextLine().trim();
        if (title.isBlank()) title = existing.getTitle();

        int credit;
        while (true) {
            System.out.print("Enter new credits [" + existing.getCredit() + "]: ");
            String creditInput = scanner.nextLine().trim();
            if (creditInput.isBlank()) {
                credit = existing.getCredit();
                break;
            }
            try {
                credit = Integer.parseInt(creditInput);
                if (credit > 0) break;
                view.printWarning("Credits must be greater than 0.");
            } catch (NumberFormatException e) {
                view.printWarning("Please enter a valid integer.");
            }
        }

        existing.setTitle(title);
        existing.setCredit(credit);

        if (courseDAO.update(existing)) {
            view.printSuccess("Course updated successfully.");
        } else {
            view.printError("Failed to update course.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Delete
    // ------------------------------------------------------------------ //

    public void deleteCourse() {
        view.printHeader("Delete Course");

        int id = ConsoleUtil.readInt("Enter course ID: ");
        Course existing = courseDAO.findById(id);

        if (existing == null) {
            view.printWarning("Course not found.");
            return;
        }

        if (!ConsoleUtil.confirm("Delete course \"" + existing.getTitle() + "\"?")) {
            view.printWarning("Cancelled.");
            return;
        }

        if (courseDAO.delete(id)) {
            view.printSuccess("Course deleted successfully.");
        } else {
            view.printError("Failed to delete course.");
        }
    }
}
