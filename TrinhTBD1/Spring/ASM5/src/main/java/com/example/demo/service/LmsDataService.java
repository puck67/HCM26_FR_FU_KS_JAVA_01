package com.example.demo.service;

import com.example.demo.model.LmsUser;
import com.example.demo.model.Student;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LmsDataService {
    private static final String USERS_FILE = "users.dat";
    private static final String STUDENTS_FILE = "students.dat";

    public void saveUsers(List<LmsUser> users) {
        FileStorageHelper.saveList(USERS_FILE, users);
    }

    public List<LmsUser> getUsers() {
        return FileStorageHelper.getList(USERS_FILE);
    }

    public void saveStudents(List<Student> students) {
        FileStorageHelper.saveList(STUDENTS_FILE, students);
    }

    public List<Student> getStudents() {
        return FileStorageHelper.getList(STUDENTS_FILE);
    }
}
