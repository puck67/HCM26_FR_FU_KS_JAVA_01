package com.fpt.lms.controller;

import com.fpt.lms.model.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping("/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_course";
        }

        return "redirect:/courses/success";
    }

    @GetMapping("/success")
    public String showCreateSuccessPage() {
        return "create_success";
    }
}
