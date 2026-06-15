package asm1.controller;

import asm1.entity.Course;
import asm1.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    // Redirect trang chủ "/" về danh sách course
    @GetMapping("/")
    public String home() {
        return "redirect:/courses/management";
    }

    // Danh sách course
    @GetMapping("/courses/management")
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "courses_management";
    }

    // Task 3: GET /courses/new - Hiển thị form tạo course mới
    @GetMapping("/courses/new")
    public String showNewCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    // Task 3: POST /courses/create - Xử lý submit form
    @PostMapping("/courses/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult bindingResult) {

        // Nếu có lỗi validation, trả về lại form cùng với thông báo lỗi
        if (bindingResult.hasErrors()) {
            return "create_course";
        }

        // Nếu hợp lệ, lưu vào Database
        courseService.save(course);

        // Task 4: Redirect về trang success
        return "redirect:/courses/success";
    }

    // Task 4: GET /courses/success - Trang thông báo thành công
    @GetMapping("/courses/success")
    public String showSuccess() {
        return "create_success";
    }
}
