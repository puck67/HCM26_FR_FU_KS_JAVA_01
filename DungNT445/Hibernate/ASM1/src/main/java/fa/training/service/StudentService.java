package fa.training.service;

import fa.training.entities.Student;
import fa.training.entities.Course;
import java.util.List;

public interface StudentService {
    void createStudent(String name, int age);
    void updateStudent(int id, String name, int age);
    void deleteStudent(int id);
    Student getStudentById(int id);
    List<Student> getAllStudents();
    
    // Relationship management
    void enrollStudentInCourse(int studentId, int courseId);
    void removeStudentFromCourse(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);

    // Reports
    List<Student> findOlderThan(int age);
    List<Object[]> findAllStudentsWithCourses();
    List<Student> findByName(String name);
    List<Student> findStudentsEnrolledInCourse(int courseId);

    // Bonus queries
    List<Student> findStudentsPaginated(int pageNumber, int pageSize);
    List<Student> findStudentsNotEnrolled();
}
