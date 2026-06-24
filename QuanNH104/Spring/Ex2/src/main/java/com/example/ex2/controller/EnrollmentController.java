package com.example.ex2.controller;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;
import com.example.ex2.service.CourseService;
import com.example.ex2.service.StudentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final StudentService studentService;
    private final CourseService courseService;

    public EnrollmentController(StudentService studentService, CourseService courseService) {
        this.studentService = studentService;
        this.courseService = courseService;
    }

    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // 1. Hiển thị tất cả các lượt đăng ký khóa học
    @GetMapping
    public String listEnrollments(Model model) {
        List<Student> students = studentService.getAllStudents();
        model.addAttribute("students", students);
        return renderLayout(model, "enrollments-management", "enrollments/list :: body");
    }

    // 2. Màn hình đăng ký học phần mới
    @GetMapping("/enroll")
    public String enrollForm(Model model) {
        List<Student> students = studentService.getAllStudents();
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("students", students);
        model.addAttribute("courses", courses);
        return renderLayout(model, "enrollments-management", "enrollments/form :: body");
    }

    // 3. Thực hiện đăng ký học viên vào khóa học
    @PostMapping("/save")
    public String saveEnrollment(@RequestParam("studentId") int studentId,
                                 @RequestParam("courseId") int courseId,
                                 Model model) {
        try {
            studentService.enrollStudentInCourse(studentId, courseId);
            return "redirect:/enrollments";
        } catch (Exception e) {
            List<Student> students = studentService.getAllStudents();
            List<Course> courses = courseService.getAllCourses();
            model.addAttribute("students", students);
            model.addAttribute("courses", courses);
            model.addAttribute("errorMessage", "Đăng ký không thành công: " + e.getMessage());
            return renderLayout(model, "enrollments-management", "enrollments/form :: body");
        }
    }

    // 4. Hủy đăng ký học viên khỏi khóa học
    @PostMapping("/unenroll")
    public String unenroll(@RequestParam("studentId") int studentId,
                           @RequestParam("courseId") int courseId,
                           @RequestParam(value = "redirectUri", required = false, defaultValue = "/enrollments") String redirectUri) {
        try {
            studentService.unenrollStudentFromCourse(studentId, courseId);
        } catch (Exception ignored) {
        }
        return "redirect:" + redirectUri;
    }
}
