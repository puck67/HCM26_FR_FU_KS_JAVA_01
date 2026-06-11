package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import com.example.demo.model.Lesson;
import com.example.demo.service.CourseService;
import com.example.demo.service.LessonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class HomeController {
    private final CourseService courseService;
    private final LessonService lessonService;

    public HomeController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping("/")
    public String home() {
        return "home";
    }

    @GetMapping("/courses/new")
    public String newCourse(Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        return "course-form";
    }

    @PostMapping("/courses")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        Course savedCourse = courseService.save(course);
        redirectAttributes.addFlashAttribute("successMessage", "Add a new Course successfully");
        redirectAttributes.addFlashAttribute("savedCourseId", savedCourse.getId());
        return "redirect:/courses/new";
    }

    @GetMapping("/courses")
    public String coursesManagement(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "courses";
    }

    @GetMapping("/students")
    public String studentsManagement() {
        return "students";
    }

    @GetMapping("/courses/{courseCode}/{startDate}/lessons")
    public String lessonDetail(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) Long editId,
            Model model) {
        CourseId courseId = new CourseId(courseCode, startDate);
        model.addAttribute("course", courseService.getRequired(courseId));
        model.addAttribute("lesson", editId == null ? new Lesson() : lessonService.getForEdit(editId));
        model.addAttribute("lessons", lessonService.findByCourse(courseId));
        return "lesson-form";
    }

    @PostMapping("/courses/{courseCode}/{startDate}/lessons")
    public String saveLesson(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @ModelAttribute Lesson lesson) {
        lessonService.save(new CourseId(courseCode, startDate), lesson);
        return "redirect:/courses/" + courseCode + "/" + startDate + "/lessons";
    }

    @GetMapping("/courses/{courseCode}/{startDate}/lessons/{lessonId}/delete")
    public String deleteLesson(
            @PathVariable String courseCode,
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @PathVariable Long lessonId) {
        lessonService.delete(lessonId);
        return "redirect:/courses/" + courseCode + "/" + startDate + "/lessons";
    }
}
