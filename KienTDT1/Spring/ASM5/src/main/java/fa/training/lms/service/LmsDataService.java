package fa.training.lms.service;

import fa.training.lms.model.LmsUser;
import fa.training.lms.model.Student;

import java.util.List;

public interface LmsDataService {
    void saveUsers(List<LmsUser> users);
    void saveStudents(List<Student> students);
    List<LmsUser> getUsers();
    List<Student> getStudents();
}