package fa.training.dao.impl;

import fa.training.dao.QueryDAO;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Hibernate implementation of QueryDAO — five query techniques.
 */
public class QueryDAOImpl implements QueryDAO {

    // ------------------------------------------------------------------ //
    //  1. HQL — students older than given age
    // ------------------------------------------------------------------ //

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Student s WHERE s.age > :age ORDER BY s.age",
                    Student.class
            ).setParameter("age", age).list();
        } catch (Exception e) {
            System.err.println("[QueryDAOImpl] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ------------------------------------------------------------------ //
    //  2. HQL with Join — (Student, Course) pairs
    // ------------------------------------------------------------------ //

    @Override
    public List<Object[]> findStudentsWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT s, c FROM Student s JOIN s.courses c ORDER BY s.name, c.title",
                    Object[].class
            ).list();
        } catch (Exception e) {
            System.err.println("[QueryDAOImpl] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ------------------------------------------------------------------ //
    //  3. Named Query — find students by name (LIKE, defined on entity)
    // ------------------------------------------------------------------ //

    @Override
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        } catch (Exception e) {
            System.err.println("[QueryDAOImpl] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ------------------------------------------------------------------ //
    //  4. Criteria API — courses with credit > minCredit
    // ------------------------------------------------------------------ //

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder     cb   = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root        = cq.from(Course.class);
            cq.select(root)
              .where(cb.greaterThan(root.get("credit"), minCredit))
              .orderBy(cb.asc(root.get("credit")));
            return session.createQuery(cq).list();
        } catch (Exception e) {
            System.err.println("[QueryDAOImpl] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // ------------------------------------------------------------------ //
    //  5. Aggregation — student count per course
    // ------------------------------------------------------------------ //

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "SELECT c.title, COUNT(s) " +
                    "FROM Course c LEFT JOIN c.students s " +
                    "GROUP BY c.title ORDER BY c.title",
                    Object[].class
            ).list();
        } catch (Exception e) {
            System.err.println("[QueryDAOImpl] Error: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
