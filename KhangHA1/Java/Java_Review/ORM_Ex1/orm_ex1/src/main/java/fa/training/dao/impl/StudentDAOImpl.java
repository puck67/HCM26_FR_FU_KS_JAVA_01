package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Hibernate implementation of StudentDAO.
 * Overrides delete() to clear the many-to-many join table first.
 */
public class StudentDAOImpl
        extends BaseDAOImpl<Student, Integer>
        implements StudentDAO {

    @Override
    protected Class<Student> getEntityClass() {
        return Student.class;
    }

    // ------------------------------------------------------------------ //
    //  Override delete — clears join-table entries before removing student
    // ------------------------------------------------------------------ //

    @Override
    public boolean delete(Integer id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student == null) {
                tx.rollback();
                return false;
            }
            // Detach from all courses (owning side — clears student_course rows)
            student.getCourses().forEach(c -> c.getStudents().remove(student));
            student.getCourses().clear();
            session.remove(student);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            System.err.println("[StudentDAOImpl] Error deleting id=" + id + ": " + e.getMessage());
            return false;
        }
    }

    // ------------------------------------------------------------------ //
    //  Pagination (bonus)
    // ------------------------------------------------------------------ //

    @Override
    public List<Student> getPage(int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student ORDER BY id", Student.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        } catch (Exception e) {
            System.err.println("[StudentDAOImpl] Error paginating: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
