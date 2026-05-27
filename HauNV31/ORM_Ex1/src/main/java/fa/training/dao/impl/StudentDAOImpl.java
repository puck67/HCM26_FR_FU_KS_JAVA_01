package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.entities.Student;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void save(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.save(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void update(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.update(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student s = session.get(Student.class, id);
            if (s != null) {
                s.getCourses().forEach(c -> c.getStudents().remove(s));
                s.getCourses().clear();
                session.delete(s);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    @Override
    public Student getById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student s = session.get(Student.class, id);
            if (s != null) {
                s.getCourses().size();
            }
            return s;
        }
    }

    @Override
    public List<Student> getAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }

    @Override
    public List<Student> getAll(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> q = session.createQuery("FROM Student", Student.class);
            q.setFirstResult((page - 1) * pageSize);
            q.setMaxResults(pageSize);
            return q.list();
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> q = session.createQuery("FROM Student s WHERE s.age > :age", Student.class);
            q.setParameter("age", age);
            return q.list();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> q = session.createNamedQuery("Student.findByName", Student.class);
            q.setParameter("name", name);
            return q.list();
        }
    }

    @Override
    public List<Object[]> findStudentsWithCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s.name, c.title FROM Student s JOIN s.courses c ORDER BY s.name";
            return session.createQuery(hql, Object[].class).list();
        }
    }

    @Override
    public List<Student> findStudentsNotEnrolled() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.courses IS EMPTY";
            return session.createQuery(hql, Student.class).list();
        }
    }
}
