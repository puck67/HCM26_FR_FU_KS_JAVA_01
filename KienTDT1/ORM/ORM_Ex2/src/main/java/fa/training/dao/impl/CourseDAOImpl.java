package fa.training.dao.impl;


import fa.training.dao.CourseDAO;
import fa.training.entity.Course;
import fa.training.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.*;

import java.util.List;

public class CourseDAOImpl implements CourseDAO {

    public void save(Course c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(c);
        tx.commit();
        session.close();
    }

    public void update(Course c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(c);
        tx.commit();
        session.close();
    }

    public void delete(int id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        Course c = session.get(Course.class, id);
        if (c != null) session.remove(c);

        tx.commit();
        session.close();
    }

    public Course findById(int id) {

        Session session =
                HibernateUtil.getSessionFactory().openSession();

        Course c = session.get(Course.class, id);

        if (c != null) {
            Hibernate.initialize(c.getStudents());
        }

        session.close();

        return c;
    }

    public List<Course> findAll() {
        Session session = HibernateUtil.getSessionFactory().openSession();
        List<Course> list = session.createQuery("FROM Course", Course.class).list();
        session.close();
        return list;
    }
    // Criteria API

    public List<Course> findCoursesWithCreditGreaterThan(int credit) {

        Session session = HibernateUtil.getSessionFactory().openSession();

        CriteriaBuilder cb = session.getCriteriaBuilder();

        CriteriaQuery<Course> cq = cb.createQuery(Course.class);

        Root<Course> root = cq.from(Course.class);

        cq.select(root)
                .where(cb.gt(root.get("credit"), credit));

        List<Course> list = session.createQuery(cq).list();

        session.close();

        return list;
    }

    // Aggregation Query

    public List<Object[]> countStudentsInCourses() {

        Session session = HibernateUtil.getSessionFactory().openSession();

        List<Object[]> list = session.createQuery(
                """
                SELECT c.title, COUNT(s)
                FROM Course c
                LEFT JOIN c.students s
                GROUP BY c.title
                """,
                Object[].class
        ).list();

        session.close();

        return list;
    }
}
