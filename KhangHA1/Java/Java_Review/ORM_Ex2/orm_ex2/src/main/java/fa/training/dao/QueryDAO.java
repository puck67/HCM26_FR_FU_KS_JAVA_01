package fa.training.dao;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;

public interface QueryDAO {
    List<Student> findStudentsOlderThan(int age);
    List<Object[]> findStudentsWithCourseTitles();
    List<Student> findStudentsByName(String name);
    List<Course> findCoursesByCreditGreaterThan(int credit);
    List<Object[]> countStudentsPerCourse();
    List<Student> findStudentsByCourseId(int courseId);
}
