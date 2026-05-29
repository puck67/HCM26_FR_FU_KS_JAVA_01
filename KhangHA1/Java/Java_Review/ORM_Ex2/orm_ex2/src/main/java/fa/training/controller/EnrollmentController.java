package fa.training.controller;

import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.service.CourseService;
import fa.training.service.StudentService;
import fa.training.service.impl.CourseServiceImpl;
import fa.training.service.impl.StudentServiceImpl;
import fa.training.util.ConsoleUtil;
import fa.training.view.EnrollmentView;

import java.util.Set;

public class EnrollmentController {
    private final StudentService studentService = new StudentServiceImpl();
    private final CourseService courseService = new CourseServiceImpl();
    private final EnrollmentView view = new EnrollmentView();

    public EnrollmentView getView() {
        return view;
    }

    public void enrollStudent() {
        int eStudentId = ConsoleUtil.readInt("Enter Student ID: ");
        int eCourseId = ConsoleUtil.readInt("Enter Course ID: ");
        studentService.enrollStudentInCourse(eStudentId, eCourseId);
    }

    public void unenrollStudent() {
        int rStudentId = ConsoleUtil.readInt("Enter Student ID: ");
        int rCourseId = ConsoleUtil.readInt("Enter Course ID: ");
        studentService.removeStudentFromCourse(rStudentId, rCourseId);
    }

    public void viewCoursesOfStudent() {
        int viewStudentId = ConsoleUtil.readInt("Enter Student ID: ");
        Set<Course> courses = studentService.getCoursesOfStudent(viewStudentId);
        if (courses == null || courses.isEmpty()) {
            System.out.println("No courses found or student not found.");
        } else {
            courses.forEach(System.out::println);
        }
    }

    public void viewStudentsOfCourse() {
        int viewCourseId = ConsoleUtil.readInt("Enter Course ID: ");
        Set<Student> students = courseService.getStudentsOfCourse(viewCourseId);
        if (students == null || students.isEmpty()) {
            System.out.println("No students found or course not found.");
        } else {
            students.forEach(System.out::println);
        }
    }
}
