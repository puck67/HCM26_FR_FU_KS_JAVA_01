package org.example.dao;

import org.example.model.Course;
import org.example.model.Student;
import org.example.util.HibernateUtils;
import org.hibernate.Session;

import java.util.List;

/**
 * StudentDAO kế thừa GenericDAO để có đầy đủ CRUD cơ bản (save, findById, findAll, update, deleteById).
 * Chỉ bổ sung thêm các hàm đặc biệt riêng của Student.
 */
public class StudentDAO extends GenericDAO<Student, Long> {

    public StudentDAO() {
        super(Student.class);
    }

    // =========================================================
    // OVERRIDE HOOK: Dọn dẹp liên kết Many-to-Many trước khi xóa Student
    // =========================================================
    @Override
    protected void beforeDelete(Student student, Session session) {
        // Xóa hết liên kết trong bảng trung gian student_course
        // để tránh lỗi Foreign Key Constraint
        student.getCourses().clear();
    }

    // =========================================================
    // PHƯƠNG THỨC ĐẶC BIỆT CỦA STUDENT
    // =========================================================

    /**
     * Tìm kiếm Student theo tên (không phân biệt hoa thường).
     *
     * @param name Tên cần tìm
     * @return Danh sách Student khớp với tên
     */
    public List<Student> findByName(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE LOWER(s.name) LIKE LOWER(:name)";
            List<Student> students = session.createQuery(hql, Student.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
            // Eager load courses để tránh LazyInitializationException
            students.forEach(s -> s.getCourses().size());
            return students;
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findByName: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy danh sách Student đã đăng ký một Course cụ thể.
     *
     * @param courseId ID của Course cần tìm
     * @return Danh sách Student trong Course đó
     */
    public List<Student> findByCourseId(Long courseId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId";
            List<Student> students = session.createQuery(hql, Student.class)
                    .setParameter("courseId", courseId)
                    .list();
            students.forEach(s -> s.getCourses().size());
            return students;
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findByCourseId: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Đăng ký một Student vào một Course (thêm liên kết Many-to-Many).
     *
     * @param studentId ID của Student
     * @param courseId  ID của Course
     */
    public void enrollCourse(Long studentId, Long courseId) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                Student student = session.get(Student.class, studentId);
                Course course = session.get(Course.class, courseId);
                if (student != null && course != null) {
                    student.getCourses().add(course);
                    course.getStudents().add(student);
                    session.merge(student);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in enrollCourse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Hủy đăng ký một Student khỏi một Course (xóa liên kết Many-to-Many).
     *
     * @param studentId ID của Student
     * @param courseId  ID của Course
     */
    public void unenrollCourse(Long studentId, Long courseId) {
        org.hibernate.Transaction transaction = null;
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                Student student = session.get(Student.class, studentId);
                Course course = session.get(Course.class, courseId);
                if (student != null && course != null) {
                    student.getCourses().remove(course);
                    course.getStudents().remove(student);
                    session.merge(student);
                }
                transaction.commit();
            } catch (Exception e) {
                if (transaction != null && transaction.isActive()) transaction.rollback();
                throw e;
            }
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in unenrollCourse: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // =========================================================
    // TASK 5: HQL — Tìm Student lớn hơn một tuổi cho trước
    // =========================================================

    /**
     * [HQL Query] Tìm tất cả Student có tuổi lớn hơn giá trị cho trước.
     *
     * @param age Ngưỡng tuổi
     * @return Danh sách Student thỏa mãn
     */
    public List<Student> findOlderThan(int age) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Student s WHERE s.age > :age ORDER BY s.age DESC";
            return session.createQuery(hql, Student.class)
                    .setParameter("age", age)
                    .list();
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findOlderThan: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // TASK 5: HQL với JOIN — Lấy Student kèm danh sách Course
    // =========================================================

    /**
     * [HQL with JOIN FETCH] Lấy danh sách Student cùng với tất cả Course họ đang học.
     * Sử dụng JOIN FETCH để tránh N+1 query problem và LazyInitializationException.
     *
     * @return Danh sách Student đã được load sẵn courses
     */
    public List<Student> findStudentsWithCourses() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // JOIN FETCH tải luôn courses trong một query duy nhất
            String hql = "SELECT DISTINCT s FROM Student s LEFT JOIN FETCH s.courses";
            return session.createQuery(hql, Student.class).list();
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findStudentsWithCourses: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // TASK 5: Named Query — Tìm Student theo tên
    // =========================================================

    /**
     * [Named Query] Tìm Student theo tên sử dụng @NamedQuery được khai báo trên Entity.
     * Named Query: "Student.findByName" (khai báo trong @NamedQuery trên class Student)
     *
     * @param name Tên hoặc một phần tên cần tìm
     * @return Danh sách Student khớp tên
     */
    public List<Student> findByNamedQuery(String name) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            List<Student> students = session.createNamedQuery("Student.findByName", Student.class)
                    .setParameter("name", "%" + name + "%")
                    .list();
            students.forEach(s -> s.getCourses().size()); // Eager load
            return students;
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findByNamedQuery: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // BONUS: Pagination — Lấy Student theo trang
    // =========================================================

    /**
     * [Bonus - Pagination] Lấy danh sách Student theo trang.
     *
     * @param page     Số trang (bắt đầu từ 0)
     * @param pageSize Số lượng record mỗi trang
     * @return Danh sách Student trong trang đó
     */
    public List<Student> findAllPaged(int page, int pageSize) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            return session.createQuery("FROM Student s ORDER BY s.id", Student.class)
                    .setFirstResult(page * pageSize) // Offset
                    .setMaxResults(pageSize)          // Limit
                    .list();
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findAllPaged: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // BONUS: Tìm Student chưa đăng ký khóa học nào
    // =========================================================

    /**
     * [Bonus] Tìm tất cả Student chưa đăng ký bất kỳ Course nào.
     *
     * @return Danh sách Student chưa có Course nào
     */
    public List<Student> findNotEnrolled() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // Dùng subquery: Student mà id không có trong bảng student_course
            String hql = "FROM Student s WHERE s.courses IS EMPTY";
            return session.createQuery(hql, Student.class).list();
        } catch (Exception e) {
            System.err.println("[StudentDAO] Error in findNotEnrolled: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}
