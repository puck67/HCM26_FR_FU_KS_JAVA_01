package com.example.menu.controller;

import com.example.menu.entity.Course;
import com.example.menu.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @GetMapping({"", "/"})
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "course_list";
    }

    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course_form";
    }

    @PostMapping("/new")
    public String saveCourse(@ModelAttribute Course course, Model model) {
        courseService.saveCourse(course);
        model.addAttribute("successMessage", "Add a new Course successfully!");
        model.addAttribute("savedCourse", course);
        // keep the form with the saved course data so it doesn't clear out
        model.addAttribute("course", course);
        return "course_form";
    }
}
