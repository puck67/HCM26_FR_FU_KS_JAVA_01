package com.example.lms.controller;

import com.example.lms.entity.Course;
import com.example.lms.entity.Lesson;
import com.example.lms.service.CourseService;
import com.example.lms.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Optional;

@Controller
@RequestMapping("/courses/{courseCode}/lessons")
@RequiredArgsConstructor
public class LessonController {

    private static final Logger log = LoggerFactory.getLogger(LessonController.class);

    private final CourseService courseService;
    private final LessonService lessonService;

    @GetMapping
    public String showLessonDetail(@PathVariable String courseCode,
                                   @RequestParam("startDate") String startDateStr,
                                   Model model) {
        log.info("Request to show lesson detail: courseCode={}, startDate={}", courseCode, startDateStr);
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);

        if (courseOpt.isEmpty()) {
            log.warn("Course not found: courseCode={}, startDate={}. Redirecting.", courseCode, startDateStr);
            return "redirect:/courses/new";
        }

        Course course = courseOpt.get();
        model.addAttribute("course", course);
        model.addAttribute("lesson", new Lesson());
        model.addAttribute("lessons", lessonService.getLessonsByCourse(course));

        return "lesson_detail";
    }

    @PostMapping
    public String addNewLesson(@PathVariable String courseCode,
                               @RequestParam("startDate") String startDateStr,
                               @ModelAttribute Lesson lesson) {
        log.info("Request to add new lesson: courseCode={}, startDate={}", courseCode, startDateStr);
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);

        if (courseOpt.isPresent()) {
            lesson.setCourse(courseOpt.get());
            lessonService.saveLesson(lesson);
            log.info("Successfully added new lesson: id={}, name={}", lesson.getId(), lesson.getLessonName());
        } else {
            log.warn("Failed to find course to add lesson to: courseCode={}, startDate={}", courseCode, startDateStr);
        }

        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDateStr;
    }

    @GetMapping("/edit/{id}")
    public String showEditLessonForm(@PathVariable String courseCode,
                                     @RequestParam("startDate") String startDateStr,
                                     @PathVariable Long id,
                                     Model model) {
        log.info("Request to show edit lesson form: courseCode={}, startDate={}, lessonId={}", courseCode, startDateStr, id);
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);

        if (courseOpt.isEmpty()) {
            log.warn("Course not found: courseCode={}, startDate={}", courseCode, startDateStr);
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

        log.warn("Lesson not found: id={}. Redirecting to details.", id);
        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDateStr;
    }

    @GetMapping("/delete/{id}")
    public String removeLessonById(@PathVariable String courseCode,
                                   @RequestParam("startDate") String startDateStr,
                                   @PathVariable Long id) {
        log.info("Request to delete lesson: courseCode={}, startDate={}, lessonId={}", courseCode, startDateStr, id);
        lessonService.deleteLesson(id);
        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDateStr;
    }
}
