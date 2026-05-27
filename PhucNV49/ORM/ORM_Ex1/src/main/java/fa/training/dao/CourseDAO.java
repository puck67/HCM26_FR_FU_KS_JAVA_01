package fa.training.dao;

import fa.training.entity.Course;
import fa.training.entity.Student;
import java.util.List;
import java.util.Map;

public interface CourseDAO {
    // CRUD Operations
    void saveCourse(Course course);
    void updateCourse(Course course);
    void deleteCourse(int id);
    Course getCourseById(int id);
    List<Course> getAllCourses();

    // Advanced Query Operations
    List<Course> findCoursesWithCreditGreaterThan(int credit); // Criteria API
    Map<String, Long> getStudentCountPerCourse(); // Aggregation Query
    List<Student> getStudentsByCourse(int courseId); // Enrollment logic display
}
