package com.practice.repository.impl;

import com.practice.config.HibernateUtil;
import com.practice.entity.Course;
import com.practice.repository.CourseRepository;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class CourseRepositoryImpl extends GenericRepositoryImpl<Course, Integer> implements CourseRepository {

    public CourseRepositoryImpl() {
        super(Course.class);
    }

    @Override
    protected void initLazyCollections(Course course) {
        if (course != null && course.getStudents() != null) {
            course.getStudents().size();
        }
    }

    @Override
    protected void beforeDelete(Course course) {
        if (course != null && course.getStudents() != null) {
            course.getStudents().forEach(student -> student.getCourses().remove(course));
        }
    }


    @Override
    public List<Course> findWithCreditGreaterThan(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Using Criteria API
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
            for (Object[] result : results) {
                countMap.put((String) result[0], (Long) result[1]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return countMap;
    }
}
