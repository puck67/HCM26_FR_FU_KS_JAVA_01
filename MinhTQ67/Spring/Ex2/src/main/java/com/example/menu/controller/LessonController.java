package com.example.menu.controller;

import com.example.menu.entity.Course;
import com.example.menu.entity.Lesson;
import com.example.menu.service.CourseService;
import com.example.menu.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/courses/{courseCode}/lessons")
@RequiredArgsConstructor
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping
    public String lessonDetail(@PathVariable String courseCode,
                               @RequestParam("startDate") String startDateStr,
                               Model model) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);

        if (courseOpt.isEmpty()) {
            return "redirect:/courses/new";
        }

        Course course = courseOpt.get();
        model.addAttribute("course", course);
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("lessons", lessonService.getLessonsByCourse(course));

        return "lesson_detail";
    }

    @PostMapping
    public String saveLesson(@PathVariable String courseCode,
                             @RequestParam("startDate") String startDateStr,
                             @ModelAttribute Lesson lesson) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);

        if (courseOpt.isPresent()) {
            lesson.setCourse(courseOpt.get());
            lessonService.saveLesson(lesson);
        }

        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDateStr;
    }

    @GetMapping("/edit/{id}")
    public String editLesson(@PathVariable String courseCode,
                             @RequestParam("startDate") String startDateStr,
                             @PathVariable Long id,
                             Model model) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);

        if (courseOpt.isEmpty()) {
            return "redirect:/courses/new";
        }

        Course course = courseOpt.get();
        Optional<Lesson> lessonOpt = lessonService.getLessonById(id);

        if (lessonOpt.isPresent()) {
            model.addAttribute("course", course);
            model.addAttribute("lesson", lessonOpt.get());
            model.addAttribute("lessons", lessonService.getLessonsByCourse(course));
            return "lesson_detail";
        }

        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDateStr;
    }

    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable String courseCode,
                               @RequestParam("startDate") String startDateStr,
                               @PathVariable Long id) {
        lessonService.deleteLesson(id);
        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDateStr;
    }
}
