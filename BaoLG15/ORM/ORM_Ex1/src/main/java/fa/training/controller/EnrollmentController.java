package fa.training.controller;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.view.ConsoleView;
import java.util.List;

public class EnrollmentController {
    private final StudentDAO studentDAO;
    private final CourseDAO courseDAO;
    private final ConsoleView view;

    public EnrollmentController(StudentDAO studentDAO, CourseDAO courseDAO, ConsoleView view) {
        this.studentDAO = studentDAO;
        this.courseDAO = courseDAO;
        this.view = view;
    }

    public void handleMenu() {
        boolean back = false;
        view.printEnrollmentMenu();
        while (!back) {
            int choice = view.readInt("Enter your choice (1-6): ");
            System.out.println();
            switch (choice) {
                case 1 -> {
                    enrollStudent();
                    view.printEnrollmentMenu();
                }
                case 2 -> {
                    removeStudentFromCourse();
                    view.printEnrollmentMenu();
                }
                case 3 -> {
                    listCoursesOfStudent();
                    view.printEnrollmentMenu();
                }
                case 4 -> {
                    listStudentsInCourse();
                    view.printEnrollmentMenu();
                }
                case 5 -> {
                    listAllEnrollments();
                    view.printEnrollmentMenu();
                }
                case 6 -> back = true;
                default -> view.displayMessage("Invalid option. Please enter a choice between 1 and 6.");
            }
            System.out.println();
        }
    }

    private void enrollStudent() {
        view.displayMessage("--- Enroll Student ---");
        int studentId = view.readInt("Enter Student ID: ");
        if (studentDAO.findById(studentId) == null) {
            view.displayMessage("Error: Student with ID " + studentId + " does not exist.");
            return;
        }
        int courseId = view.readInt("Enter Course ID: ");
        if (courseDAO.findById(courseId) == null) {
            view.displayMessage("Error: Course with ID " + courseId + " does not exist.");
            return;
        }
        try {
            studentDAO.enrollStudentInCourse(studentId, courseId);
            view.displayMessage("Enrollment request processed successfully.");
        } catch (Exception e) {
            view.displayMessage("Failed to enroll student: " + e.getMessage());
        }
    }

    private void removeStudentFromCourse() {
        view.displayMessage("--- Remove Student from Course ---");
        int studentId = view.readInt("Enter Student ID: ");
        if (studentDAO.findById(studentId) == null) {
            view.displayMessage("Error: Student with ID " + studentId + " does not exist.");
            return;
        }
        int courseId = view.readInt("Enter Course ID: ");
        if (courseDAO.findById(courseId) == null) {
            view.displayMessage("Error: Course with ID " + courseId + " does not exist.");
            return;
        }
        try {
            studentDAO.removeStudentFromCourse(studentId, courseId);
            view.displayMessage("Removal request processed successfully.");
        } catch (Exception e) {
            view.displayMessage("Failed to remove student: " + e.getMessage());
        }
    }

    private void listCoursesOfStudent() {
        view.displayMessage("--- Courses of Student ---");
        int studentId = view.readInt("Enter Student ID: ");
        List<Course> courses = studentDAO.getCoursesByStudent(studentId);
        if (courses.isEmpty()) {
            view.displayMessage("This student is not enrolled in any course.");
        } else {
            courses.forEach(c -> view.displayMessage(" - " + c));
        }
    }

    private void listStudentsInCourse() {
        view.displayMessage("--- Students Enrolled in Course ---");
        int courseId = view.readInt("Enter Course ID: ");
        List<Student> students = courseDAO.getStudentsByCourse(courseId);
        if (students.isEmpty()) {
            view.displayMessage("No students enrolled in this course.");
        } else {
            students.forEach(s -> view.displayMessage(" - " + s));
        }
    }

    private void listAllEnrollments() {
        view.displayMessage("--- All Student Enrollments (HQL Join) ---");
        List<Object[]> list = studentDAO.listStudentsAndCourses();
        view.displayEnrollments(list);
    }
}
