package fa.training.dao.impl;

import fa.training.dao.QueryDAO;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Collections;
import java.util.List;

public class QueryDAOImpl implements QueryDAO {

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.age > :age";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("age", age);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Object[]> findStudentsWithCourseTitles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s, c.title FROM Student s JOIN s.courses c";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", name);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Course> findCoursesByCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);
            cr.select(root).where(cb.greaterThan(root.get("credit"), credit));

            Query<Course> query = session.createQuery(cr);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c.title, COUNT(s.id) " +
                         "FROM Course c LEFT JOIN c.students s " +
                         "GROUP BY c.id, c.title";
            Query<Object[]> query = session.createQuery(hql, Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("courseId", courseId);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
