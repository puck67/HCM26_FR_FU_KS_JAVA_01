package service;

import entity.Course;
import entity.Student;

import java.util.List;

public interface CourseService {
    void createCourse(String title, int credit);
    void updateCourse(int id, String title, int credit);
    void deleteCourse(int id);
    Course getCourseById(int id);
    List<Course> getAllCourses();
    List<Student> getStudentsOfCourse(int courseId);

    // Advanced queries
    List<Course> findCoursesWithCreditGreaterThan(int credit);
    List<Object[]> countStudentsPerCourse();
}
