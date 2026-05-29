package com.example.dao.impl;

import com.example.dao.CourseDAO;
import com.example.entity.Course;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * CourseDAOImpl extends GenericDAOImpl and provides Course-specific DAO methods.
 */
public class CourseDAOImpl extends GenericDAOImpl<Course, Integer> implements CourseDAO {

    public CourseDAOImpl() {
        super(Course.class);
    }

    /** Initialize lazy-loaded students collection within active session */
    @Override
    protected void initLazyCollections(Course course) {
        if (course != null && course.getStudents() != null) {
            course.getStudents().size(); // trigger proxy initialization
        }
    }

    /** Dissociate course from all students before deletion to avoid FK constraint */
    @Override
    protected void beforeDelete(Course course) {
        if (course != null && course.getStudents() != null) {
            course.getStudents().forEach(student -> student.getCourses().remove(course));
            course.getStudents().clear();
        }
    }

    @Override
    public List<Course> findWithCreditGreaterThan(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Using JPA Criteria API
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.greaterThan(root.get("credit"), minCredit));

            Query<Course> query = session.createQuery(cq);
            List<Course> courses = query.getResultList();
            for (Course c : courses) {
                c.getStudents().size();
            }
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    @Override
    public Map<String, Long> getStudentCountPerCourse() {
        Map<String, Long> countMap = new LinkedHashMap<>();
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // HQL Aggregation Join Query
            String hql = "SELECT c.title, COUNT(s) " +
                         "FROM Course c LEFT JOIN c.students s " +
                         "GROUP BY c.id, c.title";
            List<Object[]> results = session.createQuery(hql, Object[].class).list();
            for (Object[] row : results) {
                countMap.put((String) row[0], (Long) row[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countMap;
    }
}
