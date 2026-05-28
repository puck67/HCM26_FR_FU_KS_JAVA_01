package com.example.service.impl;

import com.example.dao.CourseDAO;
import com.example.dao.StudentDAO;
import com.example.dao.impl.CourseDAOImpl;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import org.hibernate.Hibernate;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {
    private final StudentDAO studentDAO = new StudentDAOImpl();
    private final CourseDAO courseDAO = new CourseDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        studentDAO.save(new Student(name, age));
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Student student = studentDAO.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
        } else {
            System.out.println("❌ Không tìm thấy học sinh với ID: " + id);
        }
    }

    @Override
    public void deleteStudent(int id) {
        if (studentDAO.findById(id) != null) {
            studentDAO.delete(id);
        } else {
            System.out.println("❌ Không tìm thấy học sinh với ID: " + id);
        }
    }

    @Override
    public Student getStudentById(int id) {
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        // Cần thực hiện transaction phối hợp hoặc nạp các proxy collection đồng nhất
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            
            if (student != null && course != null) {
                student.addCourse(course);
                session.merge(student);
                tx.commit();
                System.out.println("✅ Đăng ký học phần thành công!");
            } else {
                System.out.println("❌ Thất bại: ID Học sinh hoặc ID Môn học không hợp lệ.");
                tx.rollback();
            }
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            org.hibernate.Transaction tx = session.beginTransaction();
            
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            
            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
                tx.commit();
                System.out.println("✅ Hủy đăng ký học phần thành công!");
            } else {
                System.out.println("❌ Thất bại: Liên kết không tồn tại hoặc sai ID.");
                tx.rollback();
            }
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                Hibernate.initialize(student.getCourses()); // Khởi tạo Lazy Collection trước khi đóng session
                return student.getCourses();
            }
            return Collections.emptySet();
        }
    }

    @Override public List<Student> findOlderThan(int age) { return studentDAO.findOlderThan(age); }
    @Override public List<Object[]> getAllStudentsWithCourseTitles() { return studentDAO.findAllStudentsWithCourseTitles(); }
    @Override public List<Student> findByName(String name) { return studentDAO.findByNameNamedQuery(name); }
    @Override public List<Student> findStudentsByCourseId(int courseId) { return studentDAO.findStudentsByCourseId(courseId); }
}
