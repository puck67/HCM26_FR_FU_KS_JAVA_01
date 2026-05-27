package fa.training.dao.impl;

import fa.training.dao.CourseDAO;
import fa.training.entity.Course;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Hibernate implementation of CourseDAO.
 * Overrides delete() to detach students (inverse side) before removing the course.
 */
public class CourseDAOImpl
        extends BaseDAOImpl<Course, Integer>
        implements CourseDAO {

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }

    // ------------------------------------------------------------------ //
    //  Override delete — removes course from each student's courses set first
    // ------------------------------------------------------------------ //

    @Override
    public boolean delete(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course == null) {
                tx.rollback();
                return false;
            }
            // Students own the join table; remove course from each student's set
            course.getStudents().forEach(s -> s.getCourses().remove(course));
            course.getStudents().clear();
            session.remove(course);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[CourseDAOImpl] Error deleting id=" + id + ": " + e.getMessage());
            return false;
        }
    }
}
