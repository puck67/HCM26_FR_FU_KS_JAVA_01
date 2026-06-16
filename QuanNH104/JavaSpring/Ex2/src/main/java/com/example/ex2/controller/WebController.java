package com.example.ex2.controller;

import com.example.ex2.entity.Student;
import com.example.ex2.service.CourseService;
import com.example.ex2.service.StudentService;
import com.example.ex2.service.MenuService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WebController {

    private final StudentService studentService;
    private final CourseService courseService;
    private final MenuService menuService;

    public WebController(StudentService studentService, CourseService courseService, MenuService menuService) {
        this.studentService = studentService;
        this.courseService = courseService;
        this.menuService = menuService;
    }

    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // Trang chủ hiển thị Dashboard và thống kê Menu
    @GetMapping("/")
    public String home(Model model) {
        List<Student> students = studentService.getAllStudents();
        int totalStudents = students.size();
        int totalCourses = courseService.getAllCourses().size();
        
        // Tính tổng số lượt đăng ký (enrollments)
        int totalEnrollments = 0;
        for (Student student : students) {
            totalEnrollments += student.getCourses().size();
        }

        // Thống kê Menu từ Database theo yêu cầu
        long totalMenus = menuService.getTotalMenusCount();
        long totalParentMenus = menuService.getTotalParentMenusCount();
        long totalSubMenus = menuService.getTotalSubMenusCount();

        model.addAttribute("totalStudents", totalStudents);
        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("totalEnrollments", totalEnrollments);
        
        // Thống kê dynamic menus
        model.addAttribute("totalMenus", totalMenus);
        model.addAttribute("totalParentMenus", totalParentMenus);
        model.addAttribute("totalSubMenus", totalSubMenus);

        model.addAttribute("recentStudents", students.stream().limit(5).toList());

        return renderLayout(model, "home", "home :: body");
    }

    // Endpoint để chuyển đổi vai trò (Role-based menu demo)
    @GetMapping("/switch-role")
    public String switchRole(@RequestParam("role") String role, HttpServletRequest request) {
        HttpSession session = request.getSession();
        if ("ADMIN".equalsIgnoreCase(role) || "TEACHER".equalsIgnoreCase(role) || "STUDENT".equalsIgnoreCase(role)) {
            session.setAttribute("currentRole", role.toUpperCase());
        }
        return "redirect:/";
    }
}

