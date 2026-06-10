package com.example.Ex1.controller;

import com.example.Ex1.model.Course;
import com.example.Ex1.model.CourseId;
import com.example.Ex1.model.Lesson;
import com.example.Ex1.service.CourseService;
import com.example.Ex1.service.LessonService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Controller
public class LMSController {

    private final CourseService courseService;
    private final LessonService lessonService;

    @Autowired
    public LMSController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    // 1. Homepage
    @GetMapping({"/", "/home"})
    public String showHomepage(Model model) {
        model.addAttribute("activePage", "home");
        return "home";
    }

    // 2. Add course Form (GET)
    @GetMapping("/courses/add")
    public String showAddCourseForm(@RequestParam(value = "success", required = false) Boolean success,
                                    @RequestParam(value = "courseCode", required = false) String courseCode,
                                    @RequestParam(value = "startDate", required = false) String startDateStr,
                                    Model model) {
        Course course = new Course();
        if (Boolean.TRUE.equals(success) && courseCode != null && startDateStr != null) {
            LocalDate startDate = LocalDate.parse(startDateStr);
            course = courseService.getCourseById(courseCode, startDate).orElse(new Course());
            model.addAttribute("success", true);
            model.addAttribute("savedCourseCode", courseCode);
            model.addAttribute("savedStartDate", startDateStr);
        }
        model.addAttribute("course", course);
        model.addAttribute("activePage", "add_course");
        return "add_course";
    }

    // 3. Add course (POST)
    @PostMapping("/courses/add")
    public String addCourse(@Valid @ModelAttribute("course") Course course,
                             BindingResult bindingResult,
                             Model model) {
        
        // Custom validation: check composite key uniqueness
        if (course.getCourseCode() != null && !course.getCourseCode().trim().isEmpty() && course.getStartDate() != null) {
            if (courseService.exists(course.getCourseCode(), course.getStartDate())) {
                bindingResult.rejectValue("courseCode", "duplicate", "Cặp Mã khóa học và Ngày khai giảng này đã tồn tại!");
            }
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("activePage", "add_course");
            return "add_course";
        }

        courseService.saveCourse(course);
        return "redirect:/courses/add?success=true&courseCode=" + course.getCourseCode() + "&startDate=" + course.getStartDate();
    }

    // 4. Courses Management (GET)
    @GetMapping("/courses")
    public String showCoursesList(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        model.addAttribute("activePage", "courses_list");
        return "courses_list";
    }

    // 5. Students Management (GET)
    @GetMapping("/students")
    public String showStudentsList(Model model) {
        model.addAttribute("activePage", "students_list");
        return "students_list";
    }

    // 6. Lesson Detail Screen (GET)
    @GetMapping("/lessons/detail")
    public String showLessonDetail(@RequestParam("courseCode") String courseCode,
                                   @RequestParam("startDate") String startDateStr,
                                   @RequestParam(value = "editLessonId", required = false) Long editLessonId,
                                   Model model) {
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);
        
        if (courseOpt.isEmpty()) {
            return "redirect:/home";
        }
        
        Course course = courseOpt.get();
        model.addAttribute("course", course);

        Lesson lesson = new Lesson();
        if (editLessonId != null) {
            Optional<Lesson> editLessonOpt = lessonService.getLessonById(editLessonId);
            if (editLessonOpt.isPresent() && editLessonOpt.get().getCourse().getCourseCode().equals(courseCode)
                    && editLessonOpt.get().getCourse().getStartDate().equals(startDate)) {
                lesson = editLessonOpt.get();
            }
        }
        model.addAttribute("lesson", lesson);

        // List of lessons for the table
        List<Lesson> lessons = lessonService.getLessonsByCourse(course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("activePage", "lesson_detail");
        return "lesson_detail";
    }

    // 7. Save Lesson (POST)
    @PostMapping("/lessons/save")
    public String saveLesson(@Valid @ModelAttribute("lesson") Lesson lesson,
                             BindingResult bindingResult,
                             @RequestParam("courseCode") String courseCode,
                             @RequestParam("startDate") String startDateStr,
                             Model model) {
        
        LocalDate startDate = LocalDate.parse(startDateStr);
        Optional<Course> courseOpt = courseService.getCourseById(courseCode, startDate);
        if (courseOpt.isEmpty()) {
            return "redirect:/home";
        }
        Course course = courseOpt.get();
        lesson.setCourse(course);

        if (bindingResult.hasErrors()) {
            model.addAttribute("course", course);
            List<Lesson> lessons = lessonService.getLessonsByCourse(course);
            model.addAttribute("lessons", lessons);
            model.addAttribute("activePage", "lesson_detail");
            return "lesson_detail";
        }

        lessonService.saveLesson(lesson);
        return "redirect:/lessons/detail?courseCode=" + courseCode + "&startDate=" + startDateStr;
    }

    // 8. Delete Lesson (GET)
    @GetMapping("/lessons/delete")
    public String deleteLesson(@RequestParam("id") Long id,
                               @RequestParam("courseCode") String courseCode,
                               @RequestParam("startDate") String startDateStr) {
        lessonService.deleteLessonById(id);
        return "redirect:/lessons/detail?courseCode=" + courseCode + "&startDate=" + startDateStr;
    }
}
