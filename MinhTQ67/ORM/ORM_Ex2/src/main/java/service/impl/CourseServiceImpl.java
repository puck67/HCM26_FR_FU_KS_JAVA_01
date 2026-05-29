package service.impl;

import dao.CourseDao;
import dao.impl.CourseDaoImpl;
import entity.Course;
import entity.Student;
import org.hibernate.Session;
import service.CourseService;
import util.HibernateUtil;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CourseServiceImpl implements CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);
    private final CourseDao courseDAO = new CourseDaoImpl();

    @Override
    public void createCourse(String title, int credit) {
        Course course = new Course(title, credit);
        courseDAO.save(course);
    }

    @Override
    public void updateCourse(int id, String title, int credit) {
        Course course = courseDAO.findById(id);
        if (course == null) throw new RuntimeException("Course not found with id: " + id);
        course.setTitle(title);
        course.setCredit(credit);
        courseDAO.update(course);
    }

    @Override
    public void deleteCourse(int id) {
        courseDAO.delete(id);
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
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.get(Course.class, courseId);
            if (course == null) throw new RuntimeException("Course not found with id: " + courseId);
            return new ArrayList<>(course.getStudents());
        }
    }

    // ===== Advanced Queries =====

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        // Criteria API
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.greaterThan(root.get("credit"), credit));
            return session.createQuery(cq).list();
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "SELECT c.title, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title",
                            Object[].class)
                    .list();
        }
    }
}
