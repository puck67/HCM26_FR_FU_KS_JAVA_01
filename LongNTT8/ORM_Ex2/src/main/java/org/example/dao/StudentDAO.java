package org.example.dao;

import org.example.entity.Student;
import org.hibernate.query.Query;

import java.util.List;

public class StudentDAO extends GenericDAO<Student> {

    public StudentDAO() {
        super(Student.class);
    }

    @Override
    public void save(Student student) {
        validateStudent(student);
        super.save(student);
    }

    @Override
    public void update(Student student) {
        validateStudent(student);
        super.update(student);
    }

    private void validateStudent(Student student) {
        if (student == null) throw new IllegalArgumentException("Student cannot be null");
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Student name cannot be empty");
        }
        if (student.getAge() < 0) {
            throw new IllegalArgumentException("Student age must be positive");
        }
    }

    public List<Student> findStudentsOlderThan(int age) {
        if (age < 0) throw new IllegalArgumentException("Age cannot be negative");
        return executeQueryInTransaction(session -> {
            Query<Student> query = session.createQuery("from Student s where s.age > :age", Student.class);
            query.setParameter("age", age);
            return query.list();
        });
    }

    public List<Student> findStudentsWithCourses() {
        return executeQueryInTransaction(session -> {
            return session.createQuery("select distinct s from Student s left join fetch s.courses", Student.class).list();
        });
    }

    public List<Student> findStudentsByName(String name) {
        if (name == null || name.trim().isEmpty()) throw new IllegalArgumentException("Name cannot be empty");
        return executeQueryInTransaction(session -> {
            Query<Student> query = session.createNamedQuery("Student.findByName", Student.class);
            query.setParameter("name", name);
            return query.list();
        });
    }

    public List<Student> findAllPaginated(int pageNumber, int pageSize) {
        if (pageNumber < 1) throw new IllegalArgumentException("Page number must be >= 1");
        if (pageSize < 1) throw new IllegalArgumentException("Page size must be >= 1");
        return executeQueryInTransaction(session -> {
            Query<Student> query = session.createQuery("from Student", Student.class);
            query.setFirstResult((pageNumber - 1) * pageSize);
            query.setMaxResults(pageSize);
            return query.list();
        });
    }

    public List<Student> findStudentsWithoutCourses() {
        return executeQueryInTransaction(session -> {
            return session.createQuery("from Student s where s.courses is empty", Student.class).list();
        });
    }

    public void enrollInCourse(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) throw new IllegalArgumentException("IDs must be positive");
        executeInTransaction(session -> {
            Student student = session.get(Student.class, studentId);
            org.example.entity.Course course = session.get(org.example.entity.Course.class, courseId);
            if (student == null) throw new IllegalArgumentException("Student not found");
            if (course == null) throw new IllegalArgumentException("Course not found");
            student.addCourse(course);
            session.merge(student);
        });
    }

    public void removeFromCourse(int studentId, int courseId) {
        if (studentId <= 0 || courseId <= 0) throw new IllegalArgumentException("IDs must be positive");
        executeInTransaction(session -> {
            Student student = session.get(Student.class, studentId);
            org.example.entity.Course course = session.get(org.example.entity.Course.class, courseId);
            if (student == null) throw new IllegalArgumentException("Student not found");
            if (course == null) throw new IllegalArgumentException("Course not found");
            student.removeCourse(course);
            session.merge(student);
        });
    }
}
