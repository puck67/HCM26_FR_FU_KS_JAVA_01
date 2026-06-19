package com.example.ASM5.service;

import com.example.ASM5.model.LmsUser;
import com.example.ASM5.model.Student;
import com.example.ASM5.service.base.GenericFileServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LmsDataService extends GenericFileServiceImpl<LmsUser> {
    private static final String USERS_FILE = "users.dat";
    private static final String STUDENTS_FILE = "students.dat";

    private final GenericFileServiceImpl<Student> studentFileService = new GenericFileServiceImpl<Student>() {};

    public void saveUsers(List<LmsUser> users) {
        saveAll(users, USERS_FILE);
    }

    public List<LmsUser> getUsers() {
        return getAll(USERS_FILE);
    }

    public void saveStudents(List<Student> students) {
        studentFileService.saveAll(students, STUDENTS_FILE);
    }

    public List<Student> getStudents() {
        return studentFileService.getAll(STUDENTS_FILE);
    }
}
