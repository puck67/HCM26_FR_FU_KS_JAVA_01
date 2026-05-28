package fa.training.dao;

public interface EnrollmentDAO {
    boolean enrollStudentInCourse(int studentId, int courseId);
    boolean removeStudentFromCourse(int studentId, int courseId);
}
