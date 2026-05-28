package fa.training.dao;

import fa.training.entities.Student;
import java.util.List;

public interface StudentDAO {
    void save(Student student);
    void update(Student student);
    void delete(int studentId);
    Student findById(int id);
    List<Student> findAll();
    
    // Advanced queries
    List<Student> findOlderThan(int age);
    List<Object[]> findAllStudentsWithCourses();
    List<Student> findByName(String name);
    List<Student> findStudentsInCourse(int courseId);

    // Bonus queries
    List<Student> findStudentsPaginated(int pageNumber, int pageSize);
    List<Student> findStudentsNotEnrolled();
}
