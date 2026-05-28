package com.example.dao.impl;

import com.example.dao.StudentDAO;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class StudentDAOImpl extends GenericDAOImpl<Student, Integer> implements StudentDAO {

    public StudentDAOImpl() {
        super(Student.class);
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Student s where s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Object[]> findStudentsAndCoursesHqlJoin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("select s.name, c.title from Student s join s.courses c", Object[].class).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                    "select s from Student s join s.courses c where c.id = :courseId", Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
