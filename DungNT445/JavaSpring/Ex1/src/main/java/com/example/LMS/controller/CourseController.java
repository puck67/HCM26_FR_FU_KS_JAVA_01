package com.example.LMS.controller;

import com.example.LMS.entity.Course;
import com.example.LMS.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/add";
    }

    @PostMapping("/new")
    public String addCourse(@ModelAttribute("course") Course course, RedirectAttributes redirectAttributes, Model model) {
        try {
            courseService.saveCourse(course);
            model.addAttribute("successMessage", "Add a new Course successfully");
            model.addAttribute("course", course);
            return "course/add";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Failed to save the course. Maybe the Course Code and Start Date combination already exists.");
            return "course/add";
        }
    }
}
