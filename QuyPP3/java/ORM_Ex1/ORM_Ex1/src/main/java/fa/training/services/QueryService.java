package fa.training.services;

import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.utils.HibernateUtil;
import org.hibernate.Session;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

import java.util.List;

public class QueryService {

    // 1. Find all students older than a given age
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    // 2. List students and the courses they are enrolled in
    public void listStudentsAndCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Student> students = session.createQuery(
                    "SELECT DISTINCT s FROM Student s JOIN FETCH s.courses", Student.class).list();

            System.out.println("--- List of Students and Courses they are enrolled in ---");
            for (Student s : students) {
                System.out.println("Student: " + s.getName());
                for (Course c : s.getCourses()) {
                    System.out.println("  -> Course: " + c.getTitle());
                }
            }
        }
    }

    // Find students by name
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    // 4. Find courses that have credit greater than a given value
    public List<Course> findCoursesWithCreditGreaterThan(int credit) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<Course> query = builder.createQuery(Course.class);
            Root<Course> root = query.from(Course.class);

            query.select(root).where(builder.greaterThan(root.get("credit"), credit));

            return session.createQuery(query).getResultList();
        }
    }

    // 5. Count how many students are enrolled in each course
    public void countStudentsInEachCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            List<Object[]> results = session.createQuery(
                    "SELECT c.title, count(s) FROM Course c JOIN c.students s GROUP BY c.title", Object[].class)
                    .list();

            System.out.println("--- Statistic of number of students in each course ---");
            for (Object[] result : results) {
                String courseTitle = (String) result[0];
                Long count = (Long) result[1];
                System.out.println("- " + courseTitle + ": " + count + " students");
            }
        }
    }
}
