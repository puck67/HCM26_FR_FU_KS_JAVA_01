package com.springcore.service;

import com.springcore.model.LmsUser;
import com.springcore.model.Student;

import java.util.List;

public interface LmsDataService {
    void saveUsers(List<LmsUser> users);
    List<LmsUser> getUsers();
    void saveStudents(List<Student> students);
    List<Student> getStudents();
}
