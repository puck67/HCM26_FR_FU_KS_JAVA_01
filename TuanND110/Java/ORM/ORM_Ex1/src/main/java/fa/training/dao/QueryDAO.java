package fa.training.dao;

import fa.training.entities.Course;
import fa.training.entities.Student;

import java.util.List;

public interface QueryDAO {

    List<Student> findStudentsPaged(int pageNumber, int pageSize);

    List<Student> findStudentsOlderThan(int age);

    List<Object[]> listStudentsAndCourses();

    List<Student> findStudentsByName(String name);

    List<Course> findCoursesWithCreditGreaterThan(int credit);

    List<Object[]> countStudentsPerCourse();

    List<Student> findStudentsNotEnrolledInAnyCourse();
}

