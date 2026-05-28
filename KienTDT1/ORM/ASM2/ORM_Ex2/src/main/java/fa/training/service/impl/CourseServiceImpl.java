package fa.training.service.impl;

import fa.training.dao.CourseDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.service.CourseService;

import java.util.List;
import java.util.Set;

public class CourseServiceImpl implements CourseService {

    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {

        if (title == null || title.isBlank()) {
            System.out.println("Title cannot be empty");
            return;
        }

        if (credit <= 0) {
            System.out.println("Credit must be > 0");
            return;
        }

        Course c = new Course(title, credit);

        courseDAO.save(c);

        System.out.println("Course created successfully");
    }

    @Override
    public void updateCourse(int id, String title, int credit) {

        Course c = courseDAO.findById(id);

        if (c == null) {
            System.out.println("Course not found");
            return;
        }

        c.setTitle(title);
        c.setCredit(credit);

        courseDAO.update(c);

        System.out.println("Course updated successfully");
    }

    @Override
    public void deleteCourse(int id) {

        Course c = courseDAO.findById(id);

        if (c == null) {
            System.out.println("Course not found");
            return;
        }

        courseDAO.delete(id);

        System.out.println("Course deleted successfully");
    }

    @Override
    public Course getCourseById(int id) {

        Course c = courseDAO.findById(id);

        if (c == null) {
            System.out.println("Course not found");
        }

        return c;
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public Set<Student> getStudentsOfCourse(int courseId) {

        Course c = courseDAO.findById(courseId);

        if (c == null) {
            System.out.println("Course not found");
            return Set.of();
        }

        return c.getStudents();
    }

    // HQL / Criteria methods

    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        return courseDAO.findCoursesWithCreditGreaterThan(credit);
    }

    public List<Object[]> countStudentsInCourses() {
        return courseDAO.countStudentsInCourses();
    }
}