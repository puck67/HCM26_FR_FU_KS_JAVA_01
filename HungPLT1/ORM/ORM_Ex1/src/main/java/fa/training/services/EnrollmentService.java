package fa.training.services;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;

import java.util.List;
import java.util.Set;

public class EnrollmentService {

    private final StudentDAO studentDAO = new StudentDAO();
    private final CourseDAO courseDAO = new CourseDAO();

    public void saveStudent(Student student) {
        studentDAO.save(student);
    }

    public void saveCourse(Course course) {
        courseDAO.save(course);
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public void updateCourse(Course course) {
        courseDAO.update(course);
    }

    public Student getStudentById(int id) {
        return studentDAO.getById(id);
    }

    public Course getCourseById(int id) {
        return courseDAO.getById(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAll();
    }

    public List<Course> getAllCourses() {
        return courseDAO.getAll();
    }

    public void deleteStudent(int id) {
        studentDAO.delete(id);
    }

    public void deleteCourse(int id) {
        courseDAO.delete(id);
    }

    public List<Student> getAllStudentsPaginated(int pageNumber, int pageSize) {
        return studentDAO.getAllStudentsPaginated(pageNumber, pageSize);
    }

    public void enrollStudent(int studentId, int courseId) {
        studentDAO.enrollStudent(studentId, courseId);
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        studentDAO.removeStudentFromCourse(studentId, courseId);
    }

    public Set<Course> getCoursesByStudent(int studentId) {
        return studentDAO.getCoursesByStudent(studentId);
    }

    public Set<Student> getStudentsByCourse(int courseId) {
        return studentDAO.getStudentsByCourse(courseId);
    }

    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findStudentsOlderThan(age);
    }

    public List<Student> getStudentsAndCourses() {
        return studentDAO.getStudentsAndCourses();
    }

    public List<Student> findStudentsByName(String name) {
        return studentDAO.findStudentsByName(name);
    }

    public List<Student> findStudentsWithNoCourses() {
        return studentDAO.findStudentsWithNoCourses();
    }

    public List<Course> findCoursesByCreditGreaterThan(int creditValue) {
        return courseDAO.findCoursesByCreditGreaterThan(creditValue);
    }

    public List<Object[]> getStudentCountPerCourse() {
        return courseDAO.getStudentCountPerCourse();
    }
}
