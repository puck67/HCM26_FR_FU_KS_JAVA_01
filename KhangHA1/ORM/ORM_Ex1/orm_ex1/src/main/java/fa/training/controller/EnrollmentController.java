package fa.training.controller;

import fa.training.dao.CourseDAO;
import fa.training.dao.EnrollmentDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.EnrollmentDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.ConsoleUtil;
import fa.training.view.EnrollmentView;

/**
 * Controller for enrollment operations (enroll, unenroll, list).
 */
public class EnrollmentController {

    private final EnrollmentDAO  enrollmentDAO = new EnrollmentDAOImpl();
    private final StudentDAO     studentDAO    = new StudentDAOImpl();
    private final CourseDAO      courseDAO     = new CourseDAOImpl();
    private final EnrollmentView view          = new EnrollmentView();

    public EnrollmentView getView() { return view; }

    // ------------------------------------------------------------------ //
    //  Enroll
    // ------------------------------------------------------------------ //

    public void enrollStudent() {
        view.printHeader("Enroll Student in a Course");

        int studentId = ConsoleUtil.readInt("Enter student ID: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            view.printWarning("Student not found.");
            return;
        }

        int courseId = ConsoleUtil.readInt("Enter course ID: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            view.printWarning("Course not found.");
            return;
        }

        if (enrollmentDAO.enroll(studentId, courseId)) {
            view.printSuccess("\"" + student.getName()
                    + "\" enrolled in \"" + course.getTitle() + "\".");
        } else {
            view.printError("Enrollment failed.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Unenroll
    // ------------------------------------------------------------------ //

    public void unenrollStudent() {
        view.printHeader("Remove Student from a Course");

        int studentId = ConsoleUtil.readInt("Enter student ID: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            view.printWarning("Student not found.");
            return;
        }

        int courseId = ConsoleUtil.readInt("Enter course ID: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            view.printWarning("Course not found.");
            return;
        }

        if (!ConsoleUtil.confirm("Remove \"" + student.getName()
                + "\" from \"" + course.getTitle() + "\"?")) {
            view.printWarning("Cancelled.");
            return;
        }

        if (enrollmentDAO.unenroll(studentId, courseId)) {
            view.printSuccess("Unenrolled successfully.");
        } else {
            view.printError("Unenrollment failed.");
        }
    }

    // ------------------------------------------------------------------ //
    //  Courses of a student
    // ------------------------------------------------------------------ //

    public void viewCoursesOfStudent() {
        view.printHeader("Courses of a Student");

        int studentId = ConsoleUtil.readInt("Enter student ID: ");
        Student student = studentDAO.findById(studentId);
        if (student == null) {
            view.printWarning("Student not found.");
            return;
        }

        view.displayCoursesOfStudent(
                student.getName(),
                enrollmentDAO.getCoursesOfStudent(studentId)
        );
    }

    // ------------------------------------------------------------------ //
    //  Students of a course
    // ------------------------------------------------------------------ //

    public void viewStudentsOfCourse() {
        view.printHeader("Students in a Course");

        int courseId = ConsoleUtil.readInt("Enter course ID: ");
        Course course = courseDAO.findById(courseId);
        if (course == null) {
            view.printWarning("Course not found.");
            return;
        }

        view.displayStudentsOfCourse(
                course.getTitle(),
                enrollmentDAO.getStudentsOfCourse(courseId)
        );
    }

    // ------------------------------------------------------------------ //
    //  Students not enrolled (bonus)
    // ------------------------------------------------------------------ //

    public void viewUnenrolledStudents() {
        view.displayUnenrolledStudents(enrollmentDAO.findStudentsNotEnrolled());
    }
}
