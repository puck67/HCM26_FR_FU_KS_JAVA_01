package dao;

import entity.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ultil.HibernateUtil;

import java.util.List;

public class StudentDao {

    // CREATE
    public Student save(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
            return student;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // UPDATE
    public Student update(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student merged = (Student) session.merge(student);
            tx.commit();
            return merged;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // DELETE
    public void delete(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                // Remove student from all course associations before deleting
                student.getCourses().forEach(c -> c.getStudents().remove(student));
                student.getCourses().clear();
                session.remove(student);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw e;
        }
    }

    // GET BY ID
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    // GET ALL
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }

    // GET ALL WITH PAGINATION (Bonus)
    public List<Student> findAllPaged(int page, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student ORDER BY id", Student.class)
                    .setFirstResult((page - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        }
    }
}

