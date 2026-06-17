package com.lms.controller;

import com.lms.entity.Course;
import com.lms.entity.Lesson;
import com.lms.service.LMSService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LMSController {

    private final LMSService lmsService;

    // Helper method to wrap templates in layout
    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // 1. Home Page
    @GetMapping("/")
    public String home(Model model) {
        return renderLayout(model, "home", "index :: body");
    }

    // 5. Lesson Detail Screen
    @GetMapping("/lesson/detail")
    public String lessonDetail(@RequestParam("courseCode") String courseCode,
                               @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(value = "editLessonId", required = false) Long editLessonId,
                               Model model) {
        
        Optional<Course> courseOpt = lmsService.getCourseById(courseCode, startDate);
        if (courseOpt.isEmpty()) {
            return "redirect:/courses";
        }

        List<Lesson> lessons = lmsService.getLessonsByCourse(courseCode, startDate);
        model.addAttribute("lessons", lessons);
        model.addAttribute("courseCode", courseCode);
        model.addAttribute("startDate", startDate);

        // Populate Lesson object for the form
        if (editLessonId != null) {
            Optional<Lesson> lessonOpt = lmsService.getLessonById(editLessonId);
            lessonOpt.ifPresent(lesson -> model.addAttribute("lesson", lesson));
        } else {
            model.addAttribute("lesson", new Lesson());
        }

        return renderLayout(model, "courses-management", "lesson-detail :: body");
    }

    // 6. Save/Update Lesson
    @PostMapping("/lesson/save")
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson,
                             @RequestParam("courseCode") String courseCode,
                             @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        Optional<Course> courseOpt = lmsService.getCourseById(courseCode, startDate);
        if (courseOpt.isPresent()) {
            lesson.setCourse(courseOpt.get());
            lmsService.saveLesson(lesson);
        }
        return "redirect:/lesson/detail?courseCode=" + courseCode + "&startDate=" + startDate;
    }

    // 7. Delete Lesson
    @GetMapping("/lesson/delete")
    public String deleteLesson(@RequestParam("id") Long id,
                               @RequestParam("courseCode") String courseCode,
                               @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        lmsService.deleteLesson(id);
        return "redirect:/lesson/detail?courseCode=" + courseCode + "&startDate=" + startDate;
    }


}
