package fa.training.dao;

import fa.training.model.Student;
import java.util.List;

public interface StudentDAO {
    void create(Student student);

    void update(Student student);

    void delete(String id);

    Student getStudentById(String id);

    List<Student> getAllStudents();

    boolean isEmailExists(String email, String excludeId);

    boolean isPhoneExists(String phone, String excludeId);
}
