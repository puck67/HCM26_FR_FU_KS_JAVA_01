package fa.training.service;

import fa.training.dao.StudentDAO;
import fa.training.dao.impl.StudentDAOImpl;
import fa.training.model.Student;
import java.util.List;

public class StudentService {

    private final StudentDAO studentDAO = new StudentDAOImpl();

    public void createStudent(Student student) {
        studentDAO.create(student);
    }

    public void updateStudent(Student student) {
        studentDAO.update(student);
    }

    public void deleteStudent(String id) {
        studentDAO.delete(id);
    }

    public Student getStudentById(String id) {
        return studentDAO.getStudentById(id);
    }

    public List<Student> getAllStudents() {
        return studentDAO.getAllStudents();
    }

    public boolean isEmailExists(String email, String excludeId) {
        return studentDAO.isEmailExists(email, excludeId);
    }

    public boolean isPhoneExists(String phone, String excludeId) {
        return studentDAO.isPhoneExists(phone, excludeId);
    }
}
