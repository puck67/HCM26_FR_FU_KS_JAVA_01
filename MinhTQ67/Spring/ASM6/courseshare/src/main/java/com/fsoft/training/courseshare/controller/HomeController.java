package com.fsoft.training.courseshare.controller;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.service.CategoryService;
import com.fsoft.training.courseshare.service.CourseService;
import com.fsoft.training.courseshare.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        Page<Course> coursePage = courseService.getPublishedCourses(PageRequest.of(page - 1, pageSize));
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", categoryService.getAllCategories());
        model.addAttribute("recentReviews", reviewService.getRecentApprovedReviews());
        
        return "home";
    }
}
