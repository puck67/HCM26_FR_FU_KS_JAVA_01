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
import java.util.Optional;
import java.util.Set;

public class StudentServiceImpl implements StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    @Override
    public void createStudent(String name, int age) {
        Student student = new Student(name, age);
        studentDAO.save(student);
    }

    @Override
    public void updateStudent(int id, String name, int age) {
        Optional.ofNullable(studentDAO.findById(id)).ifPresent(student -> {
            student.setName(name);
            student.setAge(age);
            studentDAO.update(student);
        });
    }

    @Override
    public void deleteStudent(int id) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Optional.ofNullable(session.get(Student.class, id)).ifPresent(student -> {
                student.getCourses().forEach(course -> course.getStudents().remove(student));
                session.remove(student);
            });
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
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
            Optional<Student> studentOpt = Optional.ofNullable(session.get(Student.class, studentId));
            Optional<Course> courseOpt = Optional.ofNullable(session.get(Course.class, courseId));

            studentOpt.ifPresent(student -> 
                courseOpt.ifPresent(course -> {
                    student.addCourse(course);
                    session.merge(student);
                })
            );
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Optional<Student> studentOpt = Optional.ofNullable(session.get(Student.class, studentId));
            Optional<Course> courseOpt = Optional.ofNullable(session.get(Course.class, courseId));

            studentOpt.ifPresent(student -> 
                courseOpt.ifPresent(course -> {
                    student.removeCourse(course);
                    session.merge(student);
                })
            );
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        }
    }

    @Override
    public Set<Course> getCoursesOfStudent(int studentId) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return Optional.ofNullable(session.get(Student.class, studentId))
                    .map(student -> {
                        student.getCourses().size();
                        return new HashSet<>(student.getCourses());
                    })
                    .orElseGet(HashSet::new);
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
        return studentDAO.findByName(name);
    }

    @Override
    public List<Object[]> getStudentsWithCourses() {
        return studentDAO.findAllWithCourses();
    }

    @Override
    public List<Student> getStudentsEnrolledInCourse(int courseId) {
        return studentDAO.findStudentsEnrolledInCourse(courseId);
    }
}
