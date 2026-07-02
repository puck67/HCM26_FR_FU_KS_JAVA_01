package com.lms.controller;

import com.lms.model.Course;
import com.lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    // List all courses
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "course/list";
    }

    // Show add-course form
    @GetMapping("/new")
    public String showAddForm(Model model) {
        model.addAttribute("course", new Course());
        return "course/detail";
    }

    // Save new course
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute Course course, Model model) {
        boolean saved = courseService.save(course);
        if (!saved) {
            model.addAttribute("error",
                "Course với mã '" + course.getCourseCode() +
                "' và ngày '" + course.getStartDate() + "' đã tồn tại!");
            model.addAttribute("course", course);
            return "course/detail";
        }
        model.addAttribute("alert", "Add a new Course successfully!");
        model.addAttribute("savedCourse", course);
        model.addAttribute("courses", courseService.findAll());
        return "course/list";
    }

    // Delete course
    @GetMapping("/delete")
    public String deleteCourse(
            @RequestParam String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        courseService.delete(code, date);
        return "redirect:/courses";
    }

    // View course detail (for lesson navigation)
    @GetMapping("/view")
    public String viewCourse(
            @RequestParam String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {
        Optional<Course> course = courseService.findById(code, date);
        course.ifPresent(c -> model.addAttribute("course", c));
        return "course/detail";
    }
}
