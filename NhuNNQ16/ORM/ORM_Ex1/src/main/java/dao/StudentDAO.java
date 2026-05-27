package dao;

import model.Course;
import model.Student;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import java.util.ArrayList;
import java.util.List;

public class StudentDAO {

    //  CRUD (TASK 3)

    public void saveStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.persist(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Student getStudentById(int id) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Student.class, id);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Tích hợp Bonus: Phân trang (Pagination) khi lấy toàn bộ học sinh
    public List<Student> getAllStudents(int pageNumber, int pageSize) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student", Student.class)
                    .setFirstResult((pageNumber - 1) * pageSize)
                    .setMaxResults(pageSize)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public void updateStudent(Student student) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.merge(student);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteStudent(int id) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, id);
            if (student != null) {
                // Xóa mối liên kết đăng ký học trước khi xóa thực thể để tránh ràng buộc CSDL
                for (Course course : new ArrayList<>(student.getCourses())) {
                    student.removeCourse(course);
                }
                session.remove(student);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Enroll student (TASK 4) ===

    public void enrollStudentInCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                student.addCourse(course); // Sử dụng helper method bảo đảm đồng bộ
                session.merge(student);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
            }

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // show course of 1 student
    public List<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            // Sử dụng JOIN FETCH để giải quyết lỗi Lazy Initialization khi truy cập bên ngoài session
            Student student = session.createQuery(
                            "SELECT s FROM Student s LEFT JOIN FETCH s.courses WHERE s.id = :id", Student.class)
                    .setParameter("id", studentId)
                    .uniqueResult();
            return student != null ? new ArrayList<>(student.getCourses()) : new ArrayList<>();
        }
    }

    // HIBERNATE QUERY PRACTICE (TASK 5)

    // 1. HQL: Tìm sinh viên lớn hơn age
    public List<Student> findStudentsOlderThan(int age) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.age > :age", Student.class)
                    .setParameter("age", age)
                    .list();
        }
    }

    // 2. HQL with Join: Danh sách sinh viên kèm môn học
    public List<Student> getStudentsAndCoursesHQLJoin() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses", Student.class)
                    .list();
        }
    }

    // 3. Named Query: Tìm học sinh theo tên
    public List<Student> findStudentsByName(String name) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", name)
                    .list();
        }
    }

    // 5. Aggregation Query: Thống kê số sinh viên đăng ký mỗi môn
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            String hql = "SELECT c.title, COUNT(s.id) FROM Course c LEFT JOIN c.students s GROUP BY c.id, c.title";
            return session.createQuery(hql, Object[].class).list();
        }
    }

    // Bonus: Tìm học sinh chưa đăng ký môn nào
    public List<Student> findStudentsWithNoCourses() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s WHERE s.courses IS EMPTY", Student.class).list();
        }
    }
}
