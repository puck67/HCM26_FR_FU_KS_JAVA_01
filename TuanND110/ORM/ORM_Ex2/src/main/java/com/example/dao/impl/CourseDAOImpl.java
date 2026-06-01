package com.example.dao.impl;
import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.List;
public class CourseDAOImpl implements CourseDAO {
    @Override
    public void save(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(course);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction);
            throw ex;
        }
    }
    @Override
    public void update(Course course) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(course);
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction);
            throw ex;
        }
    }
    @Override
    public void delete(int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, courseId);
            if (course != null) {
                session.remove(course);
            }
            transaction.commit();
        } catch (Exception ex) {
            rollback(transaction);
            throw ex;
        }
    }
    @Override
    public Course findById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct c from Course c left join fetch c.students where c.id = :id",
                            Course.class)
                    .setParameter("id", id)
                    .uniqueResult();
        }
    }
    @Override
    public List<Course> findAll() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select distinct c from Course c left join fetch c.students order by c.title",
                            Course.class)
                    .getResultList();
        }
    }
    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder criteriaBuilder = session.getCriteriaBuilder();
            CriteriaQuery<Course> criteriaQuery = criteriaBuilder.createQuery(Course.class);
            Root<Course> root = criteriaQuery.from(Course.class);
            root.fetch("students", jakarta.persistence.criteria.JoinType.LEFT);
            criteriaQuery.select(root)
                    .distinct(true)
                    .where(criteriaBuilder.greaterThan(root.get("credit"), credit))
                    .orderBy(criteriaBuilder.asc(root.get("title")));
            return session.createQuery(criteriaQuery).getResultList();
        }
    }
    @Override
    public List<Object[]> countStudentsInEachCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                            "select c, count(distinct s.id) from Course c left join c.students s group by c order by c.title",
                            Object[].class)
                    .getResultList();
        }
    }
    private void rollback(Transaction transaction) {
        if (transaction != null && transaction.isActive()) {
            transaction.rollback();
        }
    }
}
