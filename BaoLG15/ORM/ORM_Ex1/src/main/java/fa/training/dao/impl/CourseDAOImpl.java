package fa.training.dao.impl;

import fa.training.dao.CourseDAO;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseDAOImpl extends GenericDAOImpl<Course, Integer> implements CourseDAO {

    public CourseDAOImpl() {
        super(Course.class);
    }

    @Override
    protected void cleanupBeforeDelete(Session session, Course course) {
        for (Student student : course.getStudents()) {
            student.getCourses().remove(course);
        }
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.createQuery(
                    "SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id", Course.class)
                    .setParameter("id", courseId)
                    .uniqueResult();
            return course != null ? new ArrayList<>(course.getStudents()) : Collections.emptyList();
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);
            cr.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cr).getResultList();
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT c.title, COUNT(s.id) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title",
                    Object[].class)
                    .list();
        }
    }
}
