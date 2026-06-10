package com.example.EX1.controller;

import com.example.EX1.controller.base.GenericController;
import com.example.EX1.model.Student;
import com.example.EX1.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/students")
public class StudentController extends GenericController<Student, Long> {

    public StudentController(StudentService studentService) {
        super(studentService, "students", "student");
    }

    @Override
    protected Class<Student> getEntityClass() {
        return Student.class;
    }

    @GetMapping
    public String listStudents(Model model) {
        return handleList(model);
    }

    @GetMapping("/new")
    public String showAddForm(Model model) throws Exception {
        return handleCreate(model);
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        return handleEdit(id, model);
    }

    @PostMapping("/save")
    public String saveStudent(@ModelAttribute Student student, RedirectAttributes redirectAttributes) {
        service.save(student);
        redirectAttributes.addFlashAttribute("successMessage", "Student saved successfully!");
        return "redirect:/students";
    }

    @GetMapping("/{id}/delete")
    public String deleteStudent(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        service.deleteById(id);
        redirectAttributes.addFlashAttribute("successMessage", "Student deleted successfully!");
        return "redirect:/students";
    }
}
