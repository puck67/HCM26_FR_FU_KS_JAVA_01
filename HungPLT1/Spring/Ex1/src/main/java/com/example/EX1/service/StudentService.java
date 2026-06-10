package com.example.EX1.service;

import com.example.EX1.model.Student;
import com.example.EX1.repository.StudentRepository;
import com.example.EX1.service.base.GenericServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class StudentService extends GenericServiceImpl<Student, Long> {

    public StudentService(StudentRepository repository) {
        super(repository);
    }
}
