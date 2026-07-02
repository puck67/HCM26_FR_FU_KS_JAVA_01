package com.fsoft.lms.controller;

import com.fsoft.lms.model.Course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Controller xử lý luồng tạo Course mới.
 * Áp dụng PRG (Post-Redirect-Get) để tránh double-submit khi người dùng F5.
 */
@Controller
@RequestMapping("/courses")
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    /** GET /courses/new — Hiển thị form tạo Course mới */
    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    /** POST /courses/create — Xử lý submit form, áp dụng validation */
    @PostMapping("/create")
    public String addNewCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult result) {
        if (result.hasErrors()) {
            log.warn("Course form submitted with {} validation error(s)", result.getErrorCount());
            return "create_course";
        }
        log.info("New course created: title='{}', instructor='{}'",
                course.getTitle(), course.getInstructorName());
        // PRG: redirect thay vì render trực tiếp để tránh double-submit khi F5
        return "redirect:/courses/success";
    }

    /** GET /courses/success — Trang xác nhận sau khi tạo Course thành công */
    @GetMapping("/success")
    public String showAddCourseSuccess() {
        return "create_success";
    }
}
