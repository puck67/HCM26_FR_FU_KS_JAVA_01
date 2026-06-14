package com.example.Ex2.controller;

import com.example.Ex2.service.MenuService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @Autowired
    private MenuService menuService;

    private void populateModel(Model model, HttpSession session, String entityName, String pageTitle, String createBtnText) {
        if (session.getAttribute("currentRole") == null) {
            session.setAttribute("currentRole", "ADMIN");
        }
        model.addAttribute("sidebarMenus", menuService.getTopLevelMenus());
        model.addAttribute("entityName", entityName);
        model.addAttribute("pageTitle", pageTitle);
        model.addAttribute("createButtonText", createBtnText);
    }

    @GetMapping("/students")
    public String students(HttpSession session, Model model) {
        populateModel(model, session, "students", "Quản Lý Sinh Viên", "Thêm Sinh Viên");
        return "dummy-page";
    }

    @GetMapping("/lecturers")
    public String lecturers(HttpSession session, Model model) {
        populateModel(model, session, "lecturers", "Quản Lý Giảng Viên", "Thêm Giảng Viên");
        return "dummy-page";
    }

    @GetMapping("/subjects")
    public String subjects(HttpSession session, Model model) {
        populateModel(model, session, "subjects", "Quản Lý Môn Học", "Thêm Môn Học");
        return "dummy-page";
    }

    @GetMapping("/courses")
    public String courses(HttpSession session, Model model) {
        populateModel(model, session, "courses", "Quản Lý Khóa Học", "Thêm Khóa Học");
        return "dummy-page";
    }

    @GetMapping("/courses/online")
    public String coursesOnline(HttpSession session, Model model) {
        populateModel(model, session, "courses/online", "Khóa Học Trực Tuyến", "Đăng Ký Khóa Học Online");
        return "dummy-page";
    }

    @GetMapping("/courses/offline")
    public String coursesOffline(HttpSession session, Model model) {
        populateModel(model, session, "courses/offline", "Khóa Học Trực Tiếp", "Đăng Ký Khóa Học Offline");
        return "dummy-page";
    }

    @GetMapping("/questions")
    public String questions(HttpSession session, Model model) {
        populateModel(model, session, "questions", "Ngân Hàng Câu Hỏi", "Thêm Câu Hỏi");
        return "dummy-page";
    }

    @GetMapping("/exams")
    public String exams(HttpSession session, Model model) {
        populateModel(model, session, "exams", "Quản Lý Kỳ Thi", "Tạo Kỳ Thi");
        return "dummy-page";
    }

    @GetMapping("/results")
    public String results(HttpSession session, Model model) {
        populateModel(model, session, "results", "Kết Quả Học Tập", "Xem Chi Tiết Kết Quả");
        return "dummy-page";
    }

    @GetMapping("/roles")
    public String roles(HttpSession session, Model model) {
        populateModel(model, session, "roles", "Quản Lý Vai Trò", "Thêm Vai Trò");
        return "dummy-page";
    }

    @GetMapping("/users")
    public String users(HttpSession session, Model model) {
        populateModel(model, session, "users", "Quản Lý Người Dùng", "Thêm Người Dùng");
        return "dummy-page";
    }
}
