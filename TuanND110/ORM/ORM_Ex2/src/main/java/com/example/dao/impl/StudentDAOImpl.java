package com.example.dao.impl;
import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
public class StudentDAOImpl implements StudentDAO {
    @Override
    public void save(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction);
            throw ex;
        }
    }
    @Override
    public void update(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction);
            throw ex;
        }
    }
    @Override
    public void delete(int studentId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction);
            throw ex;
        }
    }
    @Override
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct s from Student s left join fetch s.courses where s.id = :id",
                            Student.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct s from Student s left join fetch s.courses order by s.name",
                            Student.class)
                    .getResultList();
        }
    }
    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct s from Student s left join fetch s.courses where s.age > :age order by s.age desc, s.name",
                            Student.class)
                    .setParameter("age", age)
                    .getResultList();
        }
    }
    @Override
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .getResultList();
        }
    }
    @Override
    public List<Object[]> listStudentsWithCourseTitles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select s, c.title from Student s join s.courses c order by s.name, c.title",
                            Object[].class)
                    .getResultList();
        }
    }
    @Override
    public List<Student> findStudentsByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct s from Student s join fetch s.courses c where c.id = :courseId order by s.name",
                            Student.class)
                    .setParameter("courseId", courseId)
                    .getResultList();
        }
    }
    private void rollback(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
