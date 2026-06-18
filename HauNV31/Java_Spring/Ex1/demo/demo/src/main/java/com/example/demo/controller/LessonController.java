package com.example.demo.controller;

import com.example.demo.model.ContentType;
import com.example.demo.model.Course;
import com.example.demo.model.CourseId;
import com.example.demo.model.Lesson;
import com.example.demo.model.LessonStatus;
import com.example.demo.service.CourseService;
import com.example.demo.service.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;

    public LessonController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping("/{courseCode}/lessons")
    public String showLessonForm(@PathVariable String courseCode,
                                 @RequestParam("startDate") LocalDate startDate,
                                 @RequestParam(value = "editId", required = false) Long editId,
                                 Model model) {
        CourseId courseId = new CourseId(courseCode, startDate);
        Course course = courseService.getCourseById(courseCode, startDate)
                .orElseThrow(() -> new IllegalArgumentException("Course not found: " + courseCode + "/" + startDate));

        List<Lesson> lessons = lessonService.getLessonsByCourse(courseCode, startDate);

        Lesson lesson = new Lesson();
        lesson.setCourse(course);
        lesson.setContentType(ContentType.VIDEO);
        lesson.setStatus(LessonStatus.OPEN);

        if (editId != null) {
            Lesson existing = lessonService.getLessonById(editId).orElse(null);
            if (existing != null) {
                lesson = existing;
            }
        }

        model.addAttribute("course", course);
        model.addAttribute("lesson", lesson);
        model.addAttribute("lessons", lessons);
        model.addAttribute("contentTypes", ContentType.values());
        model.addAttribute("statuses", LessonStatus.values());
        model.addAttribute("pageTitle", "Lesson Detail");
        model.addAttribute("activeMenu", "add-course");
        model.addAttribute("editMode", editId != null);
        model.addAttribute("editId", editId);

        return "lesson/form";
    }

    @PostMapping("/{courseCode}/lessons/save")
    public String saveLesson(@PathVariable String courseCode,
                             @RequestParam("startDate") LocalDate startDate,
                             @RequestParam("lessonName") String lessonName,
                             @RequestParam("duration") Integer duration,
                             @RequestParam("contentType") ContentType contentType,
                             @RequestParam("status") LessonStatus status,
                             @RequestParam(value = "editId", required = false) Long editId) {
        Course course = courseService.getCourseById(courseCode, startDate)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (editId != null) {
            lessonService.updateLesson(editId, lessonName, duration, contentType, status);
        } else {
            Lesson lesson = new Lesson(lessonName, duration, contentType, status);
            lesson.setCourse(course);
            lessonService.saveLesson(lesson);
        }

        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDate;
    }

    @GetMapping("/{courseCode}/lessons/{id}/edit")
    public String editLesson(@PathVariable String courseCode,
                             @RequestParam("startDate") LocalDate startDate,
                             @PathVariable("id") Long id) {
        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDate + "&editId=" + id;
    }

    @GetMapping("/{courseCode}/lessons/{id}/delete")
    public String deleteLesson(@PathVariable String courseCode,
                               @RequestParam("startDate") LocalDate startDate,
                               @PathVariable("id") Long id) {
        lessonService.deleteLesson(id);
        return "redirect:/courses/" + courseCode + "/lessons?startDate=" + startDate;
    }
}
