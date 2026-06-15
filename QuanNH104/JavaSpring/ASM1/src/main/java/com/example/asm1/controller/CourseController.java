package com.example.asm1.controller;

import com.example.asm1.model.Course;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course, 
                               BindingResult bindingResult, 
                               Model model) {
        if (bindingResult.hasErrors()) {
            return "create_course";
        }
        return "redirect:/courses/success";
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "create_success";
    }
}
