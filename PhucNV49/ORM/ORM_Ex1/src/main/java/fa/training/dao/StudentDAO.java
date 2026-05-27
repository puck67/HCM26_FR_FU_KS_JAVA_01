package fa.training.dao;

import fa.training.entity.Student;
import java.util.List;

public interface StudentDAO {
    // CRUD Operations
    void saveStudent(Student student);
    void updateStudent(Student student);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();

    // Pagination (Bonus)
    List<Student> getAllStudentsPaginated(int pageNo, int pageSize);

    // Enrollment Operations
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);

    // Advanced Query Operations
    List<Student> findStudentsOlderThan(int age);
    List<Object[]> findStudentsAndTheirCourses();
    List<Student> findStudentsByNameNamedQuery(String name);
    List<Student> findStudentsNotEnrolled(); // Bonus
}
