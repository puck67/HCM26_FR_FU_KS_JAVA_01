package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import java.util.List;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void save(Student student) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void update(Student student) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public void delete(int studentId) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    @Override
    public Student findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }

    @Override
    public List<Student> findByAgeGreaterThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.age > :age";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("age", age);
            return query.list();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", name);
            return query.list();
        }
    }

    @Override
    public List<Object[]> findAllStudentsWithCourseTitles() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s.name, c.title FROM Student s JOIN s.courses c";
            return session.createQuery(hql, Object[].class).list();
        }
    }

    @Override
    public List<Student> findByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId";
            Query<Student> query = session.createQuery(hql, Student.class);
            query.setParameter("courseId", courseId);
            return query.list();
        }
    }

    @Override
    public List<Student> findAllPaginated(int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student", Student.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
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
