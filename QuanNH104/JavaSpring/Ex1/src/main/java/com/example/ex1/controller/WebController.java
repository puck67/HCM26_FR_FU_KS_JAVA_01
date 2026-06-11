package com.example.ex1.controller;

import com.example.ex1.entity.Course;
import com.example.ex1.entity.CourseId;
import com.example.ex1.service.CourseService;
import com.example.ex1.validation.Validators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class WebController {

    private final CourseService courseService;

    // Constructor Injection thủ công để tránh lỗi Lombok trong IDE
    public WebController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Helper method để render giao diện thông qua layout chung
    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // Trang chủ
    @GetMapping("/")
    public String home(Model model) {
        return renderLayout(model, "home", "home :: body");
    }

    // Trang Thêm mới Khóa học - Trả về form trống ban đầu
    @GetMapping("/courses/create")
    public String createCourseForm(Model model) {
        Course course = new Course();
        course.setId(new CourseId());
        model.addAttribute("course", course);
        return renderLayout(model, "courses-create", "courses-create :: body");
    }

    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute("course") Course course, org.springframework.validation.BindingResult bindingResult, Model model) {
        Validators.validateCourse(course, bindingResult);
        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Dữ liệu nhập vào không hợp lệ!");
            model.addAttribute("course", course);
            model.addAttribute("showLessonDetailLink", false);
            return renderLayout(model, "courses-create", "courses-create :: body");
        }

        CourseId id = course.getId();
        
        // Kiểm tra xem cặp khóa chính đã tồn tại chưa
        if (id != null && id.getCourseCode() != null && id.getStartDate() != null) {
            if (courseService.existsById(id)) {
                model.addAttribute("errorMessage", "Khóa học với mã và ngày khai giảng này đã tồn tại!");
                model.addAttribute("course", course);
                model.addAttribute("showLessonDetailLink", false);
                return renderLayout(model, "courses-create", "courses-create :: body");
            }
            
            // Tiến hành lưu vào CSDL
            courseService.save(course);
            model.addAttribute("successMessage", "Add a new Course successfully!");
            model.addAttribute("course", course);
            model.addAttribute("showLessonDetailLink", true);
            model.addAttribute("lessonDetailUrl", "/lesson/detail?courseCode=" + id.getCourseCode() + "&startDate=" + id.getStartDate());
            return renderLayout(model, "courses-create", "courses-create :: body");
        }
        
        model.addAttribute("errorMessage", "Thông tin khóa chính không hợp lệ!");
        model.addAttribute("course", course);
        return renderLayout(model, "courses-create", "courses-create :: body");
    }
    // Trang Quản lý Khóa học
    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return renderLayout(model, "courses-management", "courses-management :: body");
    }

    // Trang Quản lý Học viên (Placeholder)
    @GetMapping("/students")
    public String manageStudents(Model model) {
        return renderLayout(model, "students-management", "students-management :: body");
    }
}
