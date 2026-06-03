package fa.training.dao.impl;

import fa.training.dao.StudentDAO;
import fa.training.entity.Student;

public class StudentDAOImpl extends BaseDAOImpl<Student, Integer> implements StudentDAO {

    public StudentDAOImpl() {
        super(Student.class);
    }
}
