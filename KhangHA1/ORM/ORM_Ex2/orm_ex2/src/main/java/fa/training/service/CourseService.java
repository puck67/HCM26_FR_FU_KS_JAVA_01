package fa.training.service;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;
import java.util.Set;

public interface CourseService {
    void createCourse(String title, int credit);
    void updateCourse(int id, String title, int credit);
    void deleteCourse(int id);
    Course getCourseById(int id);
    List<Course> getAllCourses();
    
    Set<Student> getStudentsOfCourse(int courseId);
    
    // Pass-through for advanced queries
    List<Course> getCoursesWithCreditGreaterThan(int credit);
    List<Object[]> getStudentCountPerCourse();
}
