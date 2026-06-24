package com.example.demo.controller;

import com.example.demo.model.Teacher;
import com.example.demo.service.TeacherService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/teachers")
public class TeacherController extends GenericCrudController<Teacher, Long> {
    public TeacherController(TeacherService service) {
        super(service, Teacher.class, "teachers", "Teacher Management", "Add Teacher");
    }
}
