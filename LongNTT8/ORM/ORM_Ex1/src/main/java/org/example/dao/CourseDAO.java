package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.entity.Course;

import java.util.List;

public class CourseDAO extends GenericDAO<Course> {

    public CourseDAO() {
        super(Course.class);
    }

    @Override
    public void save(Course course) {
        validateCourse(course);
        super.save(course);
    }

    @Override
    public void update(Course course) {
        validateCourse(course);
        super.update(course);
    }

    private void validateCourse(Course course) {
        if (course == null) throw new IllegalArgumentException("Course cannot be null");
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Course title cannot be empty");
        }
        if (course.getCredit() <= 0) {
            throw new IllegalArgumentException("Course credit must be positive");
        }
    }

    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        if (credit < 0) throw new IllegalArgumentException("Credit cannot be negative");
        return executeQueryInTransaction(session -> {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cr = cb.createQuery(Course.class);
            Root<Course> root = cr.from(Course.class);
            cr.select(root).where(cb.greaterThan(root.get("credit"), credit));
            return session.createQuery(cr).list();
        });
    }

    public List<Object[]> countStudentsPerCourse() {
        return executeQueryInTransaction(session -> {
            return session.createQuery(
                    "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title",
                    Object[].class
            ).list();
        });
    }
}
