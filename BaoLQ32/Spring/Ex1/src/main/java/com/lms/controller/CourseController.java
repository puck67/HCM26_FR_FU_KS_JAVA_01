package com.lms.controller;

import com.lms.model.Course;
import com.lms.model.CourseId;
import com.lms.service.CourseService;
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
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    // Show list of all courses
    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "course/list";
    }

    // Show form to add a new course
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("isSuccess", false);
        return "course/create";
    }

    // Save a new course
    @PostMapping("/new")
    public String saveCourse(@ModelAttribute("course") Course course,
                             BindingResult result,
                             Model model) {
        log.info("Attempting to save course: {}", course);

        // Basic validation
        if (course.getId().getCourseCode() == null || course.getId().getCourseCode().trim().isEmpty()) {
            result.rejectValue("id.courseCode", "error.course", "Mã khóa học không được để trống");
        }
        if (course.getId().getStartDate() == null) {
            result.rejectValue("id.startDate", "error.course", "Ngày khai giảng không được để trống");
        }
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            result.rejectValue("courseName", "error.course", "Tên khóa học không được để trống");
        }

        // Check if composite key already exists
        if (!result.hasErrors() && courseService.existsById(course.getId())) {
            result.reject("error.duplicate", "Khóa học với Mã khóa học và Ngày khai giảng này đã tồn tại!");
            model.addAttribute("errorMessage", "Cặp giá trị Mã khóa học + Ngày khai giảng là duy nhất!");
        }

        if (result.hasErrors()) {
            model.addAttribute("isSuccess", false);
            return "course/create";
        }

        // Save to database
        Course savedCourse = courseService.saveCourse(course);
        log.info("Course saved successfully: {}", savedCourse);

        model.addAttribute("course", savedCourse);
        model.addAttribute("isSuccess", true);
        model.addAttribute("successMessage", "Add a new Course successfully!");
        return "course/create";
    }

    // Delete a course
    @GetMapping("/delete/{courseCode}/{startDate}")
    public String deleteCourse(@PathVariable("courseCode") String courseCode,
                               @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
                               RedirectAttributes redirectAttributes) {
        CourseId courseId = new CourseId(courseCode, startDate);
        try {
            courseService.deleteCourse(courseId);
            redirectAttributes.addFlashAttribute("successMessage", "Xóa khóa học thành công!");
        } catch (Exception e) {
            log.error("Error deleting course", e);
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa khóa học này!");
        }
        return "redirect:/courses";
    }
}
