package fa.training.dao.impl;

import fa.training.dao.CourseDAO;
import fa.training.entity.Course;
import fa.training.entity.Student;
import fa.training.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CourseDAOImpl implements CourseDAO {

    @Override
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

    @Override
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

    @Override
    public void deleteCourse(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Course course = session.get(Course.class, id);
            if (course != null) {
                // Course is the inverse side, so we must remove references from Student side
                for (Student student : course.getStudents()) {
                    student.getCourses().remove(course);
                }
                course.getStudents().clear();
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

    @Override
    public Course getCourseById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return java.util.Optional.ofNullable(session.get(Course.class, id))
                    .map(course -> {
                        Hibernate.initialize(course.getStudents());
                        return course;
                    })
                    .orElse(null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Course> getAllCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Course> courses = session.createQuery("from Course", Course.class).list();
            for (Course course : courses) {
                Hibernate.initialize(course.getStudents());
            }
            return courses;
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);
            Root<Course> root = cq.from(Course.class);
            cq.select(root).where(cb.gt(root.get("credit"), credit));
            return session.createQuery(cq).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Map<String, Long> getStudentCountPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery(
                    "select c.title, count(s.id) from Course c left join c.students s group by c.id, c.title",
                    Object[].class
            ).list();
            return results.stream().collect(
                    java.util.stream.Collectors.toMap(
                            row -> (String) row[0],
                            row -> (Long) row[1]
                    )
            );
        } catch (Exception e) {
            e.printStackTrace();
            return Map.of();
        }
    }

    @Override
    public List<Student> getStudentsByCourse(int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return java.util.Optional.ofNullable(session.get(Course.class, courseId))
                    .map(course -> {
                        Hibernate.initialize(course.getStudents());
                        return List.copyOf(course.getStudents());
                    })
                    .orElse(List.of());
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}
