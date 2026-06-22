package org.example.controller;

import org.example.entity.Enrollment;
import org.example.service.CourseService;
import org.example.service.EnrollmentService;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {
    
    @Autowired
    private EnrollmentService enrollmentService;
    
    @Autowired
    private StudentService studentService;
    
    @Autowired
    private CourseService courseService;
    
    @GetMapping
    public String listEnrollments(Model model) {
        model.addAttribute("enrollments", enrollmentService.getAllEnrollments());
        return "enrollments/list";
    }
    
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("enrollment", new Enrollment());
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "enrollments/form";
    }
    
    @PostMapping("/save")
    public String saveEnrollment(@ModelAttribute Enrollment enrollment) {
        enrollmentService.saveEnrollment(enrollment);
        return "redirect:/enrollments";
    }
    
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        model.addAttribute("enrollment", enrollment);
        model.addAttribute("students", studentService.getAllStudents());
        model.addAttribute("courses", courseService.getAllCourses());
        return "enrollments/form";
    }
    
    @PostMapping("/update/{id}")
    public String updateEnrollment(@PathVariable Long id, @ModelAttribute Enrollment enrollment) {
        enrollmentService.updateEnrollment(id, enrollment);
        return "redirect:/enrollments";
    }
    
    @GetMapping("/delete/{id}")
    public String deleteEnrollment(@PathVariable Long id) {
        enrollmentService.deleteEnrollment(id);
        return "redirect:/enrollments";
    }
}
