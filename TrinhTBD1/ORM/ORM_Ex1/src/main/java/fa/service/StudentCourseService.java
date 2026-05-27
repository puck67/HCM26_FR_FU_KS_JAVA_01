package fa.service;

import fa.entities.Course;
import fa.entities.Student;
import java.util.List;

public interface StudentCourseService {

    void createStudent(Student student);
    void updateStudent(Student student);
    boolean deleteStudent(int id);
    Student findStudentById(int id);
    List<Student> getAllStudents();

    void createCourse(Course course);
    void updateCourse(Course course);
    boolean deleteCourse(int id);
    Course findCourseById(int id);
    List<Course> getAllCourses();

    boolean enroll(int studentId, int courseId);
    boolean unenroll(int studentId, int courseId);
    List<Course> getCoursesOfStudent(int studentId);
    List<Student> getStudentsOfCourse(int courseId);

    List<Student> getStudentsOlderThan(int age);
    List<Student> getStudentsWithCourses();
    List<Student> findStudentsByName(String name);
    List<Course> getCoursesWithCreditGreaterThan(int creditLimit);
    List<Object[]> getStudentCountPerCourse();
    List<Student> getStudentsPaged(int offset, int limit);
    List<Student> getStudentsWithoutCourses();

    void executeAutomatedDemo();

    void addStudentInteractive();
    void displayAllStudentsInteractive();
    void updateStudentInteractive();
    void deleteStudentInteractive();

    void addCourseInteractive();
    void displayAllCoursesInteractive();
    void updateCourseInteractive();
    void deleteCourseInteractive();

    void enrollInteractive();
    void unenrollInteractive();
    void displayCoursesOfStudentInteractive();
    void displayStudentsOfCourseInteractive();

    void findStudentsOlderThanInteractive();
    void searchStudentByNameInteractive();
    void findCoursesWithCreditGreaterThanInteractive();
    void showStudentCountPerCourseInteractive();
    void findStudentsWithNoEnrollmentsInteractive();
    void viewStudentsWithPaginationInteractive();
}
