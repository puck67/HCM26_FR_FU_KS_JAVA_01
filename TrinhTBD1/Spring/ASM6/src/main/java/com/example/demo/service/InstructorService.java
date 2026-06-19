package com.example.demo.service;

import com.example.demo.model.Instructor;
import com.example.demo.service.base.GenericService;

public interface InstructorService extends GenericService<Instructor, String> {

    boolean login(String username, String password);

    Instructor getInstructorByUsername(String username);

    void saveInstructor(Instructor instructor);
}
