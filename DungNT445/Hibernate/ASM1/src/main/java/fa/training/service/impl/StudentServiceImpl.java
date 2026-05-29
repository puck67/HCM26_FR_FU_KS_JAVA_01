package fa.training.service.impl;

import fa.training.dao.CourseDAO;
import fa.training.dao.StudentDAO;
import fa.training.dao.impl.CourseDAOImpl;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.entities.Course;
import fa.training.entities.Student;
import fa.training.service.StudentService;
import fa.training.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.ArrayList;
import java.util.List;

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
            System.out.println("⚠️ Không tìm thấy sinh viên có ID: " + id);
        }
    }

    @Override
    public void deleteStudent(int id) {
        if (studentDAO.findById(id) != null) {
            studentDAO.delete(id);
        } else {
            System.out.println("⚠️ Không tìm thấy sinh viên có ID: " + id);
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
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.getCourses().add(course);
                session.merge(student);
                tx.commit();
            } else {
                System.out.println("⚠️ Thất bại: Kiểm tra lại ID sinh viên hoặc môn học!");
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.getCourses().remove(course);
                session.merge(student);
                tx.commit();
            } else {
                System.out.println("⚠️ Thất bại: Không tìm thấy liên kết thông tin!");
            }
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            throw e;
        }
    }

    @Override
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentDAO.findById(studentId);
        if (student != null) {
            return new ArrayList<>(student.getCourses());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Student> findOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Object[]> findAllStudentsWithCourses() {
        return studentDAO.findAllStudentsWithCourses();
    }

    @Override
    public List<Student> findByName(String name) {
        return studentDAO.findByName(name);
    }

    @Override
    public List<Student> findStudentsEnrolledInCourse(int courseId) {
        return studentDAO.findStudentsInCourse(courseId);
    }

    @Override
    public List<Student> findStudentsPaginated(int pageNumber, int pageSize) {
        return studentDAO.findStudentsPaginated(pageNumber, pageSize);
    }

    @Override
    public List<Student> findStudentsNotEnrolled() {
        return studentDAO.findStudentsNotEnrolled();
    }
}
