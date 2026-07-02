package org.example.jsfw_s_a101.controller;

import jakarta.validation.Valid;
import org.example.jsfw_s_a101.model.Course;
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

    // Task 3.1 - Serve "New Course" page
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    // Task 3.2 + 3.3 + 3.4 + 3.5 - Handle form submission with validation
    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            // Validation fails -> quay lại form kèm lỗi
            return "create_course";
        }
        // Validation thành công -> redirect sang trang success
        return "redirect:/courses/success";
    }

    @GetMapping("/success")
    public String showSuccess() {
        return "create_success";
    }
}
