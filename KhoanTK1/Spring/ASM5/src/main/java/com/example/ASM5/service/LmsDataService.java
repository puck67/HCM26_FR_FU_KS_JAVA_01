package com.example.ASM5.service;

import com.example.ASM5.model.LmsUser;
import com.example.ASM5.model.Student;
import com.example.ASM5.service.base.GenericFileServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LmsDataService extends GenericFileServiceImpl<LmsUser> {
    private static final String usersPath = "users.dat";
    private static final String studentsPath = "students.dat";

    private final GenericFileServiceImpl<Student> studentSrv = new GenericFileServiceImpl<Student>() {};

    public void saveUsers(List<LmsUser> userList) {
        saveAll(userList, usersPath);
    }

    public List<LmsUser> getUsers() {
        return getAll(usersPath);
    }

    public void saveStudents(List<Student> studentList) {
        studentSrv.saveAll(studentList, studentsPath);
    }

    public List<Student> getStudents() {
        return studentSrv.getAll(studentsPath);
    }
}
