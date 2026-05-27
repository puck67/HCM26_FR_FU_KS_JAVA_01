package fa.training.dao;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;
import java.util.Set;

/**
 * DAO for enrollment operations — not a simple CRUD so it does not extend BaseDAO.
 */
public interface EnrollmentDAO {

    /** Enroll a student in a course. Returns false if either entity not found. */
    boolean enroll(int studentId, int courseId);

    /** Remove a student from a course. */
    boolean unenroll(int studentId, int courseId);

    /** Get all courses a student is enrolled in. */
    Set<Course> getCoursesOfStudent(int studentId);

    /** Get all students enrolled in a course. */
    Set<Student> getStudentsOfCourse(int courseId);

    /** Bonus: students who are not enrolled in any course. */
    List<Student> findStudentsNotEnrolled();
}
