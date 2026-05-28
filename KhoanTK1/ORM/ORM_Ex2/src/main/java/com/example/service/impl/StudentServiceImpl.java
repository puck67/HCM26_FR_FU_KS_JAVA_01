package com.example.service.impl;

import com.example.dao.StudentDAO;
import com.example.dao.impl.StudentDAOImpl;
import com.example.entity.Course;
import com.example.entity.Student;
import com.example.service.StudentService;
import com.example.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    // Khoi tao DAO de thao tac database
    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        // Validation co ban
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten sinh vien khong duoc de trong!");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Tuoi sinh vien phai lon hon 0!");
        }

        Student student = new Student(name, age);
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID khong hop le!");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Ten sinh vien khong duoc de trong!");
        }
        if (age <= 0) {
            throw new IllegalArgumentException("Tuoi sinh vien phai lon hon 0!");
        }

        // Kiem tra ton tai truoc khi cap nhat
        Student student = studentDAO.findById(id);
        if (student == null) {
            throw new IllegalArgumentException("Khong tim thay sinh vien co ID = " + id);
        }

        student.setName(name);
        student.setAge(age);
        studentDAO.update(student);
    }

    @Override
    public void deleteStudent(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID khong hop le!");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Student student = session.get(Student.class, id);
            
            if (student == null) {
                throw new IllegalArgumentException("Khong tim thay sinh vien co ID = " + id);
            }

            // Go bo moi quan he ManyToMany truoc khi xoa
            for (Course course : student.getCourses()) {
                course.getStudents().remove(student);
            }
            student.getCourses().clear();

            session.remove(student);
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }
            e.printStackTrace();
            throw new RuntimeException("Loi khi xoa sinh vien: " + e.getMessage());
        }
    }

    @Override
    public Student getStudentById(int id) {
        if (id <= 0) {
            return null;
        }
        return studentDAO.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentDAO.findAll();
    }

    @Override
    public void enrollStudentInCourse(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) {
            throw new IllegalArgumentException("ID phai lon hon 0!");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student == null) {
                throw new IllegalArgumentException("Khong tim thay sinh vien co ID = " + studentId);
            }
            if (course == null) {
                throw new IllegalArgumentException("Khong tim thay mon hoc co ID = " + courseId);
            }

            // Kiem tra trung enrollment
            if (student.getCourses().contains(course)) {
                throw new IllegalArgumentException("Sinh vien [" + student.getName() + "] da dang ky mon hoc [" + course.getTitle() + "] roi!");
            }

            // Them quan he ManyToMany
            student.addCourse(course);
            session.merge(student);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }
            e.printStackTrace();
            throw new RuntimeException("Loi khi dang ky mon hoc: " + e.getMessage());
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) {
            throw new IllegalArgumentException("ID phai lon hon 0!");
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);

            if (student == null) {
                throw new IllegalArgumentException("Khong tim thay sinh vien co ID = " + studentId);
            }
            if (course == null) {
                throw new IllegalArgumentException("Khong tim thay mon hoc co ID = " + courseId);
            }

            // Kiem tra xem co dang ky mon hoc nay chua
            if (!student.getCourses().contains(course)) {
                throw new IllegalArgumentException("Sinh vien [" + student.getName() + "] chua tung dang ky mon hoc [" + course.getTitle() + "]!");
            }

            // Huy dang ky
            student.removeCourse(course);
            session.merge(student);

            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            if (e instanceof IllegalArgumentException) {
                throw (IllegalArgumentException) e;
            }
            e.printStackTrace();
            throw new RuntimeException("Loi khi huy dang ky mon hoc: " + e.getMessage());
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        if (studentId <= 0) {
            return new HashSet<>();
        }
        
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            Student student = session.get(Student.class, studentId);
            if (student != null) {
                // Load lazily collection truoc khi dong session
                student.getCourses().size();
                return new HashSet<>(student.getCourses());
            }
            return new HashSet<>();
        } catch (Exception e) {
            e.printStackTrace();
            return new HashSet<>();
        }
    }

    @Override
    public List<Student> findStudentsOlderThan(int age) {
        return studentDAO.findOlderThan(age);
    }

    @Override
    public List<Student> findStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        return studentDAO.findByName(name);
    }

    @Override
    public List<Object[]> getStudentsWithCourses() {
        return studentDAO.findAllWithCourses();
    }

    @Override
    public List<Student> getStudentsEnrolledInCourse(int courseId) {
        if (courseId <= 0) {
            return null;
        }
        return studentDAO.findStudentsEnrolledInCourse(courseId);
    }
}
