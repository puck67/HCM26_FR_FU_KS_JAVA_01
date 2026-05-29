package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import java.util.List;

public class CourseDAOImpl extends GenericDAOImpl<Course, Integer> implements CourseDAO {

    public CourseDAOImpl() {
        super(Course.class);
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            jakarta.persistence.criteria.CriteriaBuilder cb = session.getCriteriaBuilder();
            jakarta.persistence.criteria.CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            jakarta.persistence.criteria.Root<Course> root = cr.from(Course.class);
            cr.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cr).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Object[]> countStudentsEnrolledInEachCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery(
                "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title", 
                Object[].class
            ).list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
