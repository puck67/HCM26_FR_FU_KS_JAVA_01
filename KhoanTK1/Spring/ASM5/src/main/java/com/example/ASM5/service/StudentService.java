package com.example.ASM5.service;

import com.example.ASM5.model.Student;
import com.example.ASM5.service.base.GenericFileServiceImpl;

import java.util.List;

public class StudentService extends GenericFileServiceImpl<Student> {
    private static final String studentDb = "students.dat";

    public void saveStudents(List<Student> studentsList) {
        saveAll(studentsList, studentDb);
    }

    public List<Student> getStudents() {
        return getAll(studentDb);
    }
}
