package fa.training.dao;

import fa.training.entity.Course;
import fa.training.entity.Student;

import java.util.List;

/**
 * DAO for the five Hibernate query techniques (Task 5).
 */
public interface QueryDAO {

    /** HQL — find students older than a given age. */
    List<Student> findStudentsOlderThan(int age);

    /** HQL with Join — (Student, Course) pairs for enrolled students. */
    List<Object[]> findStudentsWithCourses();

    /** Named Query — find students whose name contains the given string. */
    List<Student> findStudentsByName(String name);

    /** Criteria API — courses with credit greater than the given value. */
    List<Course> findCoursesWithCreditGreaterThan(int minCredit);

    /** Aggregation — {courseTitle (String), studentCount (Long)} per course. */
    List<Object[]> countStudentsPerCourse();
}
