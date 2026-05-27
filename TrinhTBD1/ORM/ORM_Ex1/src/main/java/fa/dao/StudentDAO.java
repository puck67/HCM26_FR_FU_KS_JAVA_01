package fa.dao;

import fa.entities.Course;
import fa.entities.Student;
import fa.util.TransactionManager;
import java.util.List;

public class StudentDAO {

    public void save(Student student) {
        TransactionManager.executeVoid(em -> em.persist(student));
    }

    public void update(Student student) {
        TransactionManager.executeVoid(em -> em.merge(student));
    }

    public boolean delete(int id) {
        return TransactionManager.execute(em -> {
            Student student = em.find(Student.class, id);
            if (student != null) {
                student.getCourses().forEach(course -> course.getStudents().remove(student));
                em.remove(student);
                return true;
            }
            return false;
        });
    }

    public Student findById(int id) {
        return TransactionManager.execute(em -> {
            Student student = em.find(Student.class, id);
            if (student != null) {
                student.getCourses().size();
            }
            return student;
        });
    }

    public List<Student> findAll() {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT s FROM Student s");
            List<Student> list = em.createQuery(jpql.toString(), Student.class).getResultList();
            list.forEach(student -> student.getCourses().size());
            return list;
        });
    }

    // --- Pagination (Bonus) ---

    public List<Student> findAllPaged(int offset, int limit) {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT s FROM Student s");
            List<Student> list = em.createQuery(jpql.toString(), Student.class)
                    .setFirstResult(offset)
                    .setMaxResults(limit)
                    .getResultList();
            list.forEach(student -> student.getCourses().size());
            return list;
        });
    }

    // --- Enrollment Logic ---

    public boolean enrollStudent(int studentId, int courseId) {
        return TransactionManager.execute(em -> {
            Student student = em.find(Student.class, studentId);
            Course course = em.find(Course.class, courseId);
            if (student != null && course != null) {
                if (!student.getCourses().contains(course)) {
                    student.addCourse(course);
                    em.merge(student);
                }
                return true;
            }
            return false;
        });
    }

    public boolean removeStudent(int studentId, int courseId) {
        return TransactionManager.execute(em -> {
            Student student = em.find(Student.class, studentId);
            Course course = em.find(Course.class, courseId);
            if (student != null && course != null) {
                if (student.getCourses().contains(course)) {
                    student.removeCourse(course);
                    em.merge(student);
                }
                return true;
            }
            return false;
        });
    }

    public List<Course> getCoursesOfStudent(int studentId) {
        return TransactionManager.execute(em -> {
            Student student = em.find(Student.class, studentId);
            if (student != null) {
                student.getCourses().size();
                return List.copyOf(student.getCourses());
            }
            return List.of();
        });
    }

    public List<Student> findOlderThan(int age) {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT s FROM Student s ")
               .append("WHERE s.age > :age");
            return em.createQuery(jpql.toString(), Student.class)
                    .setParameter("age", age)
                    .getResultList();
        });
    }

    public List<Student> findStudentsWithCourses() {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT DISTINCT s FROM Student s ")
               .append("LEFT JOIN FETCH s.courses");
            return em.createQuery(jpql.toString(), Student.class)
                    .getResultList();
        });
    }

    public List<Student> findByName(String name) {
        return TransactionManager.execute(em -> 
            em.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .getResultList()
        );
    }

    public List<Student> findStudentsWithoutCourses() {
        return TransactionManager.execute(em -> {
            StringBuilder jpql = new StringBuilder();
            jpql.append("SELECT s FROM Student s ")
               .append("WHERE s.courses IS EMPTY");
            return em.createQuery(jpql.toString(), Student.class)
                    .getResultList();
        });
    }
}
