package dao;

import entities.Course;
import entities.Student;
import java.util.List;

public interface HibernatePracticeDAO {
    List<Student> findStudentsOlderThan(int age);
    List<Object[]> findStudentsAndCoursesHqlJoin();
    List<Student> findByNameNamedQuery(String name);
    List<Course> findCoursesWithCreditGreaterThan(int credit);
    List<Object[]> countStudentsEnrolledInEachCourse();
}
