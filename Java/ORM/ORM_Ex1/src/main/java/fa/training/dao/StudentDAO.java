package fa.training.dao;

import fa.training.entities.Student;

import java.util.List;

public interface StudentDAO {
    boolean saveStudent(Student student);

    boolean updateStudent(Student student);

    boolean deleteStudent(int studentId);

    Student getStudentById(int studentId);

    List<Student> getAllStudents();
}

