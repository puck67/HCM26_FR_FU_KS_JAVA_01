package com.example.demo.controller;

import com.example.demo.model.Student;
import com.example.demo.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/students")
public class StudentController extends GenericCrudController<Student, Long> {
    public StudentController(StudentService service) {
        super(service, Student.class, "students", "Student Management", "Add Student");
    }
}
