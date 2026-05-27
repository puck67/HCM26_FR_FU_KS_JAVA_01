package fa.dao;

import fa.entities.Course;
import fa.entities.Student;
import fa.util.TransactionManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import java.util.List;

public class CourseDAO {

    public void save(Course course) {
        TransactionManager.executeVoid(em -> em.persist(course));
    }

    public void update(Course course) {
        TransactionManager.executeVoid(em -> em.merge(course));
    }

    public boolean delete(int id) {
        return TransactionManager.execute(em -> {
            Course course = em.find(Course.class, id);
            if (course != null) {
                course.getStudents().forEach(student -> student.getCourses().remove(course));
                em.remove(course);
                return true;
            }
            return false;
        });
    }

    public Course findById(int id) {
        return TransactionManager.execute(em -> {
            Course course = em.find(Course.class, id);
            if (course != null) {
                course.getStudents().size();
            }
            return course;
        });
    }

    public List<Course> findAll() {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT c FROM Course c");
            List<Course> list = em.createQuery(jpql.toString(), Course.class).getResultList();
            list.forEach(course -> course.getStudents().size());
            return list;
        });
    }

    public List<Student> getStudentsOfCourse(int courseId) {
        return TransactionManager.execute(em -> {
            Course course = em.find(Course.class, courseId);
            if (course != null) {
                course.getStudents().size();
                return List.copyOf(course.getStudents());
            }
            return List.of();
        });
    }

    public List<Course> findCoursesWithCreditGreaterThan(int creditLimit) {
        return TransactionManager.execute(em -> {
            CriteriaBuilder cb = em.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            
            cq.select(root).where(cb.gt(root.get("credit"), creditLimit));
            
            return em.createQuery(cq).getResultList();
        });
    }

    public List<Object[]> getStudentCountPerCourse() {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT c.title, COUNT(s.id) ")
               .append("FROM Course c ")
               .append("LEFT JOIN c.students s ")
               .append("GROUP BY c.title, c.id");
            return em.createQuery(jpql.toString(), Object[].class).getResultList();
        });
    }
}
