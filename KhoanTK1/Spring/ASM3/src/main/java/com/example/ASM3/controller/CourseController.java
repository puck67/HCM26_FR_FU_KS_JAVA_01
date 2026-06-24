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

    private final CourseService service;

    @Autowired
    public CourseController(CourseService service) {
        this.service = service;
    }

    @GetMapping
    public String index() {
        return "redirect:/courses";
    }

    @GetMapping("/courses")
    public String listCourses(Model m) {
        List<Course> allCourses = service.getCourses();
        m.addAttribute("courses", allCourses);
        if (!m.containsAttribute("course")) {
            m.addAttribute("course", new Course());
        }
        return "courses";
    }

    @PostMapping("/courses/add")
    public String addCourse(@ModelAttribute("course") Course data, Model m, RedirectAttributes ra) {
        boolean err = false;
        
        if (data.getTitle() == null || "".equals(data.getTitle().trim())) {
            m.addAttribute("titleError", "Tên khóa học không được để trống!");
            err = true;
        }
        if (data.getInstructorName() == null || "".equals(data.getInstructorName().trim())) {
            m.addAttribute("instructorError", "Tên giảng viên không được để trống!");
            err = true;
        }
        if (data.getDurationHours() <= 0) {
            m.addAttribute("durationError", "Thời lượng phải lớn hơn 0 giờ!");
            err = true;
        }
        if (data.getDescription() == null || "".equals(data.getDescription().trim())) {
            m.addAttribute("descriptionError", "Mô tả khóa học không được để trống!");
            err = true;
        }

        if (err) {
            m.addAttribute("courses", service.getCourses());
            m.addAttribute("errorMessage", "Vui lòng sửa các lỗi bên dưới.");
            return "courses";
        }

        service.addCourse(data);
        ra.addFlashAttribute("successMessage", "Thêm khóa học '" + data.getTitle() + "' thành công!");
        return "redirect:/courses";
    }

    @PostMapping("/courses/delete")
    public String deleteCourse(@RequestParam("title") String courseTitle, RedirectAttributes ra) {
        boolean isDone = service.deleteCourse(courseTitle);
        if (isDone) {
            ra.addFlashAttribute("successMessage", "Đã xóa khóa học thành công!");
        } else {
            ra.addFlashAttribute("errorMessage", "Không tìm thấy khóa học cần xóa!");
        }
        return "redirect:/courses";
    }
}
