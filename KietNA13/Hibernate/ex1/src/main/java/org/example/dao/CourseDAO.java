package org.example.dao;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.example.model.Course;
import org.hibernate.Session;
import org.example.util.HibernateUtils;

import java.util.List;

/**
 * CourseDAO kế thừa GenericDAO để có đầy đủ CRUD cơ bản (save, findById, findAll, update, deleteById).
 * Chỉ bổ sung thêm các hàm đặc biệt riêng của Course.
 */
public class CourseDAO extends GenericDAO<Course, Long> {

    public CourseDAO() {
        super(Course.class);
    }

    // =========================================================
    // OVERRIDE HOOK: Dọn dẹp liên kết Many-to-Many trước khi xóa Course
    // =========================================================
    @Override
    protected void beforeDelete(Course course, Session session) {
        // Xóa hết liên kết trong bảng trung gian student_course
        // để tránh lỗi Foreign Key Constraint
        course.getStudents().clear();
    }

    // =========================================================
    // PHƯƠNG THỨC ĐẶC BIỆT CỦA COURSE
    // =========================================================

    /**
     * Tìm kiếm Course theo tên (không phân biệt hoa thường).
     *
     * @param title Tên khóa học cần tìm
     * @return Danh sách Course khớp với tên
     */
    public List<Course> findByTitle(String title) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Course c WHERE LOWER(c.title) LIKE LOWER(:title)";
            List<Course> courses = session.createQuery(hql, Course.class)
                    .setParameter("title", "%" + title + "%")
                    .list();
            courses.forEach(c -> c.getStudents().size());
            return courses;
        } catch (Exception e) {
            System.err.println("[CourseDAO] Error in findByTitle: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Tìm kiếm Course theo số tín chỉ tối thiểu.
     *
     * @param minCredit Số tín chỉ tối thiểu
     * @return Danh sách Course có số tín chỉ >= minCredit
     */
    public List<Course> findByMinCredit(int minCredit) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "FROM Course c WHERE c.credit >= :minCredit ORDER BY c.credit DESC";
            return session.createQuery(hql, Course.class)
                    .setParameter("minCredit", minCredit)
                    .list();
        } catch (Exception e) {
            System.err.println("[CourseDAO] Error in findByMinCredit: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Lấy danh sách Course mà một Student đã đăng ký.
     *
     * @param studentId ID của Student cần tìm
     * @return Danh sách Course của Student đó
     */
    public List<Course> findByStudentId(Long studentId) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            String hql = "SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId";
            return session.createQuery(hql, Course.class)
                    .setParameter("studentId", studentId)
                    .list();
        } catch (Exception e) {
            System.err.println("[CourseDAO] Error in findByStudentId: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // TASK 5: Criteria API — Tìm Course có credit > giá trị cho trước
    // =========================================================

    /**
     * [Criteria API] Tìm tất cả Course có số tín chỉ lớn hơn giá trị cho trước.
     * Sử dụng JPA Criteria API thay vì HQL string — type-safe hơn, tránh lỗi typo.
     *
     * @param minCredit Ngưỡng tín chỉ (exclusive - lấy credit > minCredit)
     * @return Danh sách Course thỏa mãn, sắp xếp theo credit giảm dần
     */
    public List<Course> findByMinCreditCriteria(int minCredit) {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            CriteriaBuilder cb = session.getCriteriaBuilder();

            // Tạo CriteriaQuery cho kiểu trả về Course
            CriteriaQuery<Course> cq = cb.createQuery(Course.class);

            // Root: FROM Course c
            Root<Course> root = cq.from(Course.class);

            // SELECT c FROM Course c WHERE c.credit > minCredit ORDER BY c.credit DESC
            cq.select(root)
              .where(cb.gt(root.get("credit"), minCredit))
              .orderBy(cb.desc(root.get("credit")));

            return session.createQuery(cq).list();
        } catch (Exception e) {
            System.err.println("[CourseDAO] Error in findByMinCreditCriteria: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // =========================================================
    // TASK 5: Aggregation — Đếm số Student trong mỗi Course
    // =========================================================

    /**
     * [Aggregation Query] Đếm số lượng Student đã đăng ký cho mỗi Course.
     * Trả về danh sách Object[] với phần tử [0] là Course và [1] là số lượng Student (Long).
     *
     * Ví dụ kết quả:
     *   ["Java Programming", 3]
     *   ["Database Systems", 2]
     *
     * @return Danh sách Object[] { Course, count }
     */
    public List<Object[]> countStudentsPerCourse() {
        try (Session session = HibernateUtils.getSessionFactory().openSession()) {
            // GROUP BY và COUNT: đếm số student trong mỗi course
            // LEFT JOIN đảm bảo course không có student nào vẫn xuất hiện (count = 0)
            String hql = "SELECT c.title, COUNT(s) FROM Course c LEFT JOIN c.students s " +
                         "GROUP BY c.id, c.title ORDER BY COUNT(s) DESC";
            return session.createQuery(hql, Object[].class).list();
        } catch (Exception e) {
            System.err.println("[CourseDAO] Error in countStudentsPerCourse: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
}

