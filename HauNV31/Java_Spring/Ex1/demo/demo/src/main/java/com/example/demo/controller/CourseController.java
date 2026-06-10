package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import com.example.demo.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/new")
    public String showAddForm(@RequestParam(value = "saved", required = false) Boolean saved,
                              Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        model.addAttribute("pageTitle", "Add a new Course");
        model.addAttribute("activeMenu", "add-course");
        model.addAttribute("isNew", true);
        model.addAttribute("saved", saved != null && saved);
        return "course/form";
    }

    @PostMapping("/save")
    public String saveCourse(@RequestParam("courseCode") String courseCode,
                             @RequestParam("startDate") LocalDate startDate,
                             @RequestParam("courseName") String courseName,
                             @RequestParam("category") String category,
                             @RequestParam("instructor") String instructor,
                             RedirectAttributes redirectAttributes) {
        CourseId id = new CourseId(courseCode, startDate);
        Course course = new Course(id, courseName, category, instructor);
        courseService.saveCourse(course);

        redirectAttributes.addFlashAttribute("successMessage", "Add a new Course successfully!");
        redirectAttributes.addFlashAttribute("savedCourse", course);
        return "redirect:/courses/new?saved=true";
    }

    @GetMapping("/list")
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("pageTitle", "Courses Management");
        model.addAttribute("activeMenu", "courses");
        return "course/list";
    }

    @GetMapping("/students")
    public String studentsManagement(Model model) {
        model.addAttribute("pageTitle", "Students Management");
        model.addAttribute("activeMenu", "students");
        return "students";
    }
}
