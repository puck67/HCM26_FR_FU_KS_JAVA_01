package com.lms.controller;

import com.lms.model.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @GetMapping("/new")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/create")
    public String createCourse(@Valid Course course, BindingResult result) {
        return Optional.of(result)
                .filter(BindingResult::hasErrors)
                .map(r -> "create_course")
                .orElse("redirect:/courses/success");
    }

    @GetMapping("/success")
    public String showSuccessPage() {
        return "create_success";
    }
}
