package com.example.ASM3.controller;

import com.example.ASM3.model.Course;
import com.example.ASM3.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping
    public String index() {
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String listCourses(Model model) {
        List<Course> courses = courseService.getCourses();
        model.addAttribute("courses", courses);
        // Add an empty course object for form binding if not present
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        return "courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute("course") Course course, Model model, RedirectAttributes redirectAttributes) {

        boolean hasErrors = false;
        
        if (course.getTitle() == null || course.getTitle().trim().isEmpty()) {
            model.addAttribute("titleError", "Tên khóa học không được để trống!");
            hasErrors = true;
        }
        if (course.getInstructorName() == null || course.getInstructorName().trim().isEmpty()) {
            model.addAttribute("instructorError", "Tên giảng viên không được để trống!");
            hasErrors = true;
        }
        if (course.getDurationHours() <= 0) {
            model.addAttribute("durationError", "Thời lượng phải lớn hơn 0 giờ!");
            hasErrors = true;
        }
        if (course.getDescription() == null || course.getDescription().trim().isEmpty()) {
            model.addAttribute("descriptionError", "Mô tả khóa học không được để trống!");
            hasErrors = true;
        }

        if (hasErrors) {
            model.addAttribute("courses", courseService.getCourses());
            model.addAttribute("errorMessage", "Vui lòng sửa các lỗi bên dưới.");
            return "courses";
        }

        courseService.addCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Thêm khóa học '" + course.getTitle() + "' thành công!");
        return "redirect:/courses";
    }

    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam("title") String title, RedirectAttributes redirectAttributes) {
        boolean deleted = courseService.deleteCourse(title);
        if (deleted) {
            redirectAttributes.addFlashAttribute("successMessage", "Đã xóa khóa học thành công!");
        } else {
            redirectAttributes.addFlashAttribute("errorMessage", "Không tìm thấy khóa học cần xóa!");
        }
        return "redirect:/courses";
    }
}
