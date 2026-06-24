package com.fsoft.lms.controller;

import com.fsoft.lms.model.Course;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class CourseController {

    @GetMapping("/courses/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/courses/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                                BindingResult result) {
        if (result.hasErrors()) {
            return "create_course";
        }
        return "create_success";
    }
}
