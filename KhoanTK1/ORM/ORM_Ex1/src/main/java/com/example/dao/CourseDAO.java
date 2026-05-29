package com.example.dao;

import com.example.entity.Course;
import com.example.entity.Student;
import com.example.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CourseDAO extends GenericDAO<Course> {

    public CourseDAO() {
        super(Course.class);
    }

    // Task 4: Display all students of a given course
    public List<Student> getStudentsByCourseId(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Course course = session.createQuery(
                    "SELECT c FROM Course c LEFT JOIN FETCH c.students WHERE c.id = :id", Course.class)
                    .setParameter("id", courseId)
                    .uniqueResult();
            return course != null ? new ArrayList<>(course.getStudents()) : Collections.emptyList();
        }
    }

    // Task 5.4: Criteria API
    public List<Course> findCoursesWithCreditGreaterThan(int minCredit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);

            cq.select(root).where(cb.gt(root.get("credit"), minCredit));

            return session.createQuery(cq).list();
        }
    }

    // Task 5.5: Aggregation Query
    public void countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hqlCount = "SELECT c.title, COUNT(s.id) FROM Course c LEFT JOIN c.students s GROUP BY c.title";
            List<Object[]> countResults = session.createQuery(hqlCount, Object[].class).list();
            for (Object[] row : countResults) {
                System.out.println(row[0] + ": " + row[1] + " students");
            }
        }
    }
}
