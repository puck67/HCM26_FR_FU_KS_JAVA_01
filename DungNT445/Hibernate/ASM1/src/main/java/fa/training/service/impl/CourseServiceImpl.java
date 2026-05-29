package fa.training.service.impl;

import fa.training.dao.CourseDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.service.CourseService;
import java.util.ArrayList;
import java.util.List;

public class CourseServiceImpl implements CourseService {
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createCourse(String title, int credit) {
        courseDAO.save(new Course(title, credit));
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseDAO.update(course);
        } else {
            System.out.println("⚠️ Không tìm thấy môn học có ID: " + id);
        }
    }

    @Override
    public void deleteCourse(int id) {
        if (courseDAO.findById(id) != null) {
            courseDAO.delete(id);
        } else {
            System.out.println("⚠️ Không tìm thấy môn học có ID: " + id);
        }
    }

    @Override
    public Course getCourseById(int id) {
        return courseDAO.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseDAO.findAll();
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseDAO.findById(courseId);
        if (course != null) {
            return new ArrayList<>(course.getStudents());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int value) {
        return courseDAO.findCoursesWithCreditGreaterThan(value);
    }

    @Override
    public List<Object[]> countStudentsOfCourse() {
        return courseDAO.countStudentsPerCourse();
    }
}
