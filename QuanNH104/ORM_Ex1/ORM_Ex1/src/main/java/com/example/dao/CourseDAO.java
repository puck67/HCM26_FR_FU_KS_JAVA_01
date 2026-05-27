package com.example.dao;

import com.example.model.Course;
import com.example.model.Student;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import jakarta.persistence.criteria.*;

import java.util.List;

public class CourseDAO {

    public void saveCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void updateCourse(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteCourse(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                for (Student student : course.getStudents()) {
                    student.getCourses().remove(course);
                }
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Course getCourse(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Course.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Course", Course.class).list();
        }
    }

    public List<Student> getStudentsOfCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select s from Course c join c.students s where c.id = :courseId";
            return session.createQuery(hql, Student.class)
                    .setParameter("courseId", courseId)
                    .list();
        }
    }

    public List<Course> findCoursesWithCreditGreaterThan(int creditValue) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);
            
            cr.select(root).where(cb.gt(root.get("credit"), creditValue));
            
            return session.createQuery(cr).getResultList();
        }
    }

    public List<Object[]> getStudentCountPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "select c.title, count(s) from Course c left join c.students s group by c.title";
            return session.createQuery(hql, Object[].class).list();
        }
    }
}
