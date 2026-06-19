package fa.training.lms.service;

import fa.training.lms.model.LmsUser;
import fa.training.lms.model.Student;
import fa.training.lms.repository.LmsUserRepository;
import fa.training.lms.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class LmsDataServiceImpl implements LmsDataService {

    @Autowired
    private LmsUserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Override
    @Transactional
    public void saveUsers(List<LmsUser> users) {
        userRepository.saveAll(users);
    }

    @Override
    @Transactional
    public void saveStudents(List<Student> students) {
        studentRepository.saveAll(students);
    }

    @Override
    public List<LmsUser> getUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<Student> getStudents() {
        return studentRepository.findAll();
    }
}
