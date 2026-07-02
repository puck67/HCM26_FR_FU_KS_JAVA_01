package com.lms.controller;

import com.lms.model.Course;
import com.lms.model.Lesson;
import com.lms.service.CourseService;
import com.lms.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/lessons")
public class LessonController {

    @Autowired
    private LessonService lessonService;

    @Autowired
    private CourseService courseService;

    // Show lesson detail screen for a specific course
    @GetMapping
    public String showLessons(
            @RequestParam String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        Optional<Course> courseOpt = courseService.findById(code, date);
        if (courseOpt.isEmpty()) {
            return "redirect:/courses";
        }

        Course course = courseOpt.get();
        List<Lesson> lessons = lessonService.findByCourse(code, date);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("newLesson", new Lesson());
        return "lesson/detail";
    }

    // Save (add or update) a lesson
    @PostMapping("/save")
    public String saveLesson(
            @RequestParam String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @ModelAttribute("newLesson") Lesson lesson,
            Model model) {

        Optional<Course> courseOpt = courseService.findById(code, date);
        if (courseOpt.isEmpty()) {
            return "redirect:/courses";
        }

        lesson.setCourse(courseOpt.get());
        lessonService.save(lesson);

        return "redirect:/lessons?code=" + code + "&date=" + date;
    }

    // Load lesson data into form for editing
    @GetMapping("/edit/{id}")
    public String editLesson(
            @PathVariable Integer id,
            @RequestParam String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            Model model) {

        Optional<Course> courseOpt = courseService.findById(code, date);
        Optional<Lesson> lessonOpt = lessonService.findById(id);

        if (courseOpt.isEmpty() || lessonOpt.isEmpty()) {
            return "redirect:/courses";
        }

        List<Lesson> lessons = lessonService.findByCourse(code, date);
        model.addAttribute("course", courseOpt.get());
        model.addAttribute("lessons", lessons);
        model.addAttribute("newLesson", lessonOpt.get());
        model.addAttribute("editMode", true);
        return "lesson/detail";
    }

    // Delete a lesson
    @GetMapping("/delete/{id}")
    public String deleteLesson(
            @PathVariable Integer id,
            @RequestParam String code,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        lessonService.delete(id);
        return "redirect:/lessons?code=" + code + "&date=" + date;
    }
}
