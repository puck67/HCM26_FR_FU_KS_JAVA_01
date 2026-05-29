package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class StudentDAOImpl implements StudentDAO {

    @Override
    public void save(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Lưu student thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public void update(Student student) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.merge(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Cập nhật student thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public void delete(int studentId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student managed = session.get(Student.class, studentId);
            if (managed != null) {
                managed.getCourses().forEach(c -> c.getStudents().remove(managed));
                managed.getCourses().clear();
                session.remove(managed);
            }
            tx.commit();
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            throw new RuntimeException("Xóa student thất bại: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Student> findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Student.class, id));
        }
    }

    @Override
    public List<Student> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class).list();
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "FROM Student s WHERE s.age > :age ORDER BY s.age DESC", Student.class
            ).setParameter("age", age).list();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
        }
    }

    @Override
    public List<Object[]> findAllWithCourses() {
        String hql = "SELECT s.name, c.title FROM Student s JOIN s.courses c ORDER BY s.name";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Object[].class).list();
        }
    }

    @Override
    public List<Student> findByCourseId(int courseId) {
        String hql = "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        }
    }

    @Override
    public List<Object[]> countPerCourse() {
        String hql = "SELECT c.title, COUNT(s) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title ORDER BY c.title";
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(hql, Object[].class).list();
        }
    }
}
