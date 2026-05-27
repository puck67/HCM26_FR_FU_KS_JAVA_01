package com.practice.repository.impl;

import com.practice.config.HibernateUtil;
import com.practice.entity.Student;
import com.practice.repository.StudentRepository;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.List;

public class StudentRepositoryImpl extends GenericRepositoryImpl<Student, Integer> implements StudentRepository {

    public StudentRepositoryImpl() {
        super(Student.class);
    }

    @Override
    protected void initLazyCollections(Student student) {
        if (student != null && student.getCourses() != null) {
            student.getCourses().size();
        }
    }

    @Override
    protected void beforeDelete(Student student) {
        if (student != null && student.getCourses() != null) {
            student.getCourses().forEach(course -> course.getStudents().remove(student));
        }
    }


    @Override
    public List<Student> findAllPaginated(int offset, int limit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("from Student", Student.class);
            query.setFirstResult(offset);
            query.setMaxResults(limit);
            List<Student> students = query.list();
            for (Student s : students) {
                s.getCourses().size();
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student s WHERE s.age > :age", Student.class);
            query.setParameter("age", age);
            List<Student> students = query.list();
            for (Student s : students) {
                s.getCourses().size();
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Object[]> findStudentsAndCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Join students and courses. Since we want students and their enrolled courses:
            // "SELECT s.name, c.title FROM Student s JOIN s.courses c"
            Query<Object[]> query = session.createQuery("SELECT s, c FROM Student s JOIN s.courses c", Object[].class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Executing Named Query
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", "%" + name + "%");
            List<Student> students = query.list();
            for (Student s : students) {
                s.getCourses().size();
            }
            return students;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public List<Student> findUnenrolledStudents() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Query<Student> query = session.createQuery("FROM Student s WHERE s.courses IS EMPTY", Student.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
