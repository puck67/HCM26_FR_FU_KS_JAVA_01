package com.lms.controller;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.model.Lesson;
import com.lms.service.CourseService;
import com.lms.service.LessonService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/courses/{courseCode}/{startDate}/lessons")
@RequiredArgsConstructor
@Slf4j
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;

    // Helper to get Course from path variables
    private Course getCourse(String courseCode, LocalDate startDate) {
        CourseId courseId = new CourseId(courseCode, startDate);
        return courseService.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course key: " + courseCode + " - " + startDate));
    }

    // View lessons screen (shows course summary, add form, and list table)
    @GetMapping
    public String showLessonDetails(@PathVariable("courseCode") String courseCode,
                                    @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                                    Model model) {
        Course course = getCourse(courseCode, startDate);
        List<Lesson> lessons = lessonService.getLessonsByCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        
        // Form backing object - check if it's already in the model (e.g. from redirect or error)
        if (!model.containsAttribute("lesson")) {
            model.addAttribute("lesson", new Lesson());
        }
        
        return "lesson/detail";
    }

    // Save or update a lesson
    @PostMapping("/save")
    public String saveLesson(@PathVariable("courseCode") String courseCode,
                             @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                             @ModelAttribute("lesson") Lesson lesson,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {
        log.info("Saving lesson: {} for course: {}/{}", lesson, courseCode, startDate);
        
        Course course = getCourse(courseCode, startDate);

        if (lesson.getLessonName() == null || lesson.getLessonName().trim().isEmpty()) {
            result.rejectValue("lessonName", "error.lesson", "Tên bài học không được để trống");
        }
        if (lesson.getDuration() == null || lesson.getDuration() <= 0) {
            result.rejectValue("duration", "error.lesson", "Thời lượng phải lớn hơn 0");
        }

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.lesson", result);
            redirectAttributes.addFlashAttribute("lesson", lesson);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng kiểm tra lại thông tin nhập!");
            return String.format("redirect:/courses/%s/%s/lessons", courseCode, startDate);
        }

        // Set course reference
        lesson.setCourse(course);
        lessonService.saveLesson(lesson);
        
        redirectAttributes.addFlashAttribute("successMessage", "Lưu bài học thành công!");
        return String.format("redirect:/courses/%s/%s/lessons", courseCode, startDate);
    }

    // Edit lesson trigger (loads lesson details into form)
    @GetMapping("/edit/{id}")
    public String editLesson(@PathVariable("courseCode") String courseCode,
                             @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                             @PathVariable("id") Long id,
                             Model model) {
        Course course = getCourse(courseCode, startDate);
        Lesson lesson = lessonService.getLessonById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid lesson ID: " + id));

        List<Lesson> lessons = lessonService.getLessonsByCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("lesson", lesson); // Pre-fill form
        
        return "lesson/detail";
    }

    // Delete a lesson
    @GetMapping("/delete/{id}")
    public String deleteLesson(@PathVariable("courseCode") String courseCode,
                               @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               @PathVariable("id") Long id,
                               RedirectAttributes redirectAttributes) {
        try {
            lessonService.deleteLesson(id);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa bài học thành công!");
        } catch (Exception e) {
            log.error("Error deleting lesson", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa bài học này!");
        }
        return String.format("redirect:/courses/%s/%s/lessons", courseCode, startDate);
    }
}
