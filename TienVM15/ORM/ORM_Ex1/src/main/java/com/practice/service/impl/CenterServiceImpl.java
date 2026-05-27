package com.practice.service.impl;

import com.practice.config.HibernateUtil;
import com.practice.entity.Course;
import com.practice.entity.Student;
import com.practice.repository.CourseRepository;
import com.practice.repository.StudentRepository;
import com.practice.repository.impl.CourseRepositoryImpl;
import com.practice.repository.impl.StudentRepositoryImpl;
import com.practice.service.CenterService;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CenterServiceImpl implements CenterService {

    private final StudentRepository studentRepository = new StudentRepositoryImpl();
    private final CourseRepository courseRepository = new CourseRepositoryImpl();

    @Override
    public void createStudent(String name, int age) {
        studentRepository.save(new Student(name, age));
    }

    @Override
    public boolean updateStudent(int id, String name, int age) {
        Student student = studentRepository.findById(id);
        if (student != null) {
            student.setName(name);
            student.setAge(age);
            studentRepository.update(student);
            return true;
        } else {
            System.out.println("Student with ID " + id + " not found.");
            return false;
        }
    }

    @Override
    public boolean deleteStudent(int id) {
        Student student = studentRepository.findById(id);
        if (student != null) {
            studentRepository.delete(id);
            return true;
        } else {
            System.out.println("Student with ID " + id + " not found.");
            return false;
        }
    }

    @Override
    public Student getStudent(int id) {
        return studentRepository.findById(id);
    }

    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @Override
    public List<Student> getAllStudentsPaginated(int page, int size) {
        int offset = (page - 1) * size;
        return studentRepository.findAllPaginated(offset, size);
    }

    @Override
    public void createCourse(String title, int credit) {
        courseRepository.save(new Course(title, credit));
    }

    @Override
    public boolean updateCourse(int id, String title, int credit) {
        Course course = courseRepository.findById(id);
        if (course != null) {
            course.setTitle(title);
            course.setCredit(credit);
            courseRepository.update(course);
            return true;
        } else {
            System.out.println("Course with ID " + id + " not found.");
            return false;
        }
    }

    @Override
    public boolean deleteCourse(int id) {
        Course course = courseRepository.findById(id);
        if (course != null) {
            courseRepository.delete(id);
            return true;
        } else {
            System.out.println("Course with ID " + id + " not found.");
            return false;
        }
    }

    @Override
    public Course getCourse(int id) {
        return courseRepository.findById(id);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public void enrollStudent(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.addCourse(course);
                session.merge(student);
            } else {
                System.out.println("Enrollment failed: Student or Course not found.");
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
    public void removeStudentFromCourse(int studentId, int courseId) {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            Student student = session.get(Student.class, studentId);
            Course course = session.get(Course.class, courseId);
            if (student != null && course != null) {
                student.removeCourse(course);
                session.merge(student);
            } else {
                System.out.println("Disenrollment failed: Student or Course not found.");
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
    public List<Course> getCoursesOfStudent(int studentId) {
        Student student = studentRepository.findById(studentId);
        if (student != null) {
            return new ArrayList<>(student.getCourses());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Student> getStudentsOfCourse(int courseId) {
        Course course = courseRepository.findById(courseId);
        if (course != null) {
            return new ArrayList<>(course.getStudents());
        }
        return new ArrayList<>();
    }

    @Override
    public List<Student> getStudentsOlderThan(int age) {
        return studentRepository.findOlderThan(age);
    }

    @Override
    public List<Object[]> getStudentsAndCourses() {
        return studentRepository.findStudentsAndCourses();
    }

    @Override
    public List<Student> getStudentsByName(String name) {
        return studentRepository.findByName(name);
    }

    @Override
    public List<Course> getCoursesWithCreditGreaterThan(int credit) {
        return courseRepository.findWithCreditGreaterThan(credit);
    }

    @Override
    public Map<String, Long> getStudentCountPerCourse() {
        return courseRepository.getStudentCountPerCourse();
    }

    @Override
    public List<Student> getUnenrolledStudents() {
        return studentRepository.findUnenrolledStudents();
    }
}
