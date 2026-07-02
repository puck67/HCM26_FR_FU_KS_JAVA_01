package com.fpt.lms.controller;

import com.fpt.lms.entity.Course;
import com.fpt.lms.service.CategoryService;
import com.fpt.lms.service.CourseService;
import com.fpt.lms.service.ReviewService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public HomeController(CourseService courseService, CategoryService categoryService, ReviewService reviewService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = courseService.getPublishedCourses(page);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("recentReviews", reviewService.getRecentApprovedReviews());
        return "public/home";
    }

    @GetMapping("/login")
    public String login() {
        return "public/login";
    }
}

