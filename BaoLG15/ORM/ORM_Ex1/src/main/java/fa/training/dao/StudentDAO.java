package fa.training.dao;

import fa.training.entities.Course;
import fa.training.entities.Student;
import java.util.List;

public interface StudentDAO extends GenericDAO<Student, Integer> {
    List<Student> findAllPaginated(int pageNumber, int pageSize);

    void enrollStudentInCourse(int studentId, int courseId);

    void removeStudentFromCourse(int studentId, int courseId);

    List<Course> getCoursesByStudent(int studentId);

    List<Student> findStudentsOlderThan(int age);

    List<Object[]> listStudentsAndCourses();

    List<Student> findByName(String name);

    List<Student> findStudentsNotEnrolled();
}
