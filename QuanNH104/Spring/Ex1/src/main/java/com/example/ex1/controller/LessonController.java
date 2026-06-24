package com.example.ex1.controller;

import com.example.ex1.entity.Course;
import com.example.ex1.entity.CourseId;
import com.example.ex1.entity.Lesson;
import com.example.ex1.service.CourseService;
import com.example.ex1.service.LessonService;
import com.example.ex1.validation.Validators;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;

    // Constructor Injection thủ công để tránh lỗi Lombok trong IDE
    public LessonController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    // Helper method để render giao diện thông qua layout chung
    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // 1. Hiển thị màn hình Quản lý Chi tiết Bài học
    @GetMapping("/lesson/detail")
    public String lessonDetail(@RequestParam("courseCode") String courseCode,
                               @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                               @RequestParam(value = "editLessonId", required = false) Long editLessonId,
                               Model model) {
        
        // Kiểm tra xem khóa học có tồn tại hay không
        CourseId courseId = new CourseId(courseCode, startDate);
        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isEmpty()) {
            return "redirect:/courses";
        }

        // Lấy danh sách bài học hiện tại thuộc khóa học
        List<Lesson> lessons = lessonService.findByCourse(courseCode, startDate);
        model.addAttribute("lessons", lessons);
        model.addAttribute("courseCode", courseCode);
        model.addAttribute("startDate", startDate);

        // Chuẩn bị thực thể Lesson cho form
        if (editLessonId != null) {
            Optional<Lesson> lessonOpt = lessonService.findById(editLessonId);
            if (lessonOpt.isPresent()) {
                model.addAttribute("lesson", lessonOpt.get());
            } else {
                model.addAttribute("lesson", new Lesson());
            }
        } else {
            model.addAttribute("lesson", new Lesson());
        }

        return renderLayout(model, "courses-management", "lesson-detail :: body");
    }

    // 2. Lưu/Cập nhật bài học
    @PostMapping("/lesson/save")
    public String saveLesson(@ModelAttribute("lesson") Lesson lesson,
                             org.springframework.validation.BindingResult bindingResult,
                             @RequestParam("courseCode") String courseCode,
                             @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                             Model model) {
        
        Validators.validateLesson(lesson, bindingResult);
        
        CourseId courseId = new CourseId(courseCode, startDate);
        Optional<Course> courseOpt = courseService.findById(courseId);
        
        if (courseOpt.isEmpty()) {
            return "redirect:/courses";
        }

        if (bindingResult.hasErrors()) {
            List<Lesson> lessons = lessonService.findByCourse(courseCode, startDate);
            model.addAttribute("lessons", lessons);
            model.addAttribute("courseCode", courseCode);
            model.addAttribute("startDate", startDate);
            model.addAttribute("errorMessage", "Thông tin bài học không hợp lệ!");
            return renderLayout(model, "courses-management", "lesson-detail :: body");
        }

        lesson.setCourse(courseOpt.get());
        lessonService.save(lesson);
        
        return "redirect:/lesson/detail?courseCode=" + courseCode + "&startDate=" + startDate;
    }

    // 3. Xóa bài học
    @GetMapping("/lesson/delete")
    public String deleteLesson(@RequestParam("id") Long id,
                               @RequestParam("courseCode") String courseCode,
                               @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate) {
        
        lessonService.deleteById(id);
        return "redirect:/lesson/detail?courseCode=" + courseCode + "&startDate=" + startDate;
    }
}
