package com.example.LMS.controller;

import com.example.LMS.entity.Course;
import com.example.LMS.entity.CourseId;
import com.example.LMS.entity.Lesson;
import com.example.LMS.service.CourseService;
import com.example.LMS.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/courses/{courseCode}/{startDate}/lessons")
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @Autowired
    public LessonController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @ModelAttribute("course")
    public Course getCourse(@PathVariable String courseCode, @PathVariable LocalDate startDate) {
        CourseId id = new CourseId(courseCode, startDate);
        Optional<Course> courseOpt = courseService.getCourseById(id);
        return courseOpt.orElse(null);
    }

    @GetMapping
    public String showLessonDetail(@ModelAttribute("course") Course course, Model model) {
        if (course == null) {
            return "redirect:/"; // Redirect if course not found
        }
        
        List<Lesson> lessons = lessonService.getLessonsByCourse(course);
        model.addAttribute("lessons", lessons);
        
        if (!model.containsAttribute("lesson")) {
            model.addAttribute("lesson", new Lesson());
        }
        
        return "lesson/detail";
    }

    @PostMapping
    public String saveLesson(@ModelAttribute("course") Course course, 
                             @ModelAttribute("lesson") Lesson lesson, 
                             RedirectAttributes redirectAttributes) {
        if (course == null) {
            return "redirect:/";
        }
        
        lesson.setCourse(course);
        lessonService.saveLesson(lesson);
        
        return "redirect:/courses/" + course.getCourseCode() + "/" + course.getStartDate() + "/lessons";
    }

    @GetMapping("/{lessonId}/edit")
    public String editLesson(@PathVariable Long lessonId, 
                             @ModelAttribute("course") Course course, 
                             RedirectAttributes redirectAttributes) {
        if (course == null) {
            return "redirect:/";
        }
        
        Optional<Lesson> lessonOpt = lessonService.getLessonById(lessonId);
        if (lessonOpt.isPresent()) {
            redirectAttributes.addFlashAttribute("lesson", lessonOpt.get());
        }
        
        return "redirect:/courses/" + course.getCourseCode() + "/" + course.getStartDate() + "/lessons";
    }

    @GetMapping("/{lessonId}/delete")
    public String deleteLesson(@PathVariable Long lessonId, 
                               @ModelAttribute("course") Course course) {
        if (course == null) {
            return "redirect:/";
        }
        
        lessonService.deleteLesson(lessonId);
        
        return "redirect:/courses/" + course.getCourseCode() + "/" + course.getStartDate() + "/lessons";
    }
}
