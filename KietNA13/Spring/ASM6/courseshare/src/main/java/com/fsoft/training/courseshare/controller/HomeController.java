package com.fsoft.training.courseshare.controller;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.service.CategoryService;
import com.fsoft.training.courseshare.service.CourseService;
import com.fsoft.training.courseshare.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {

    private static final Logger log = LoggerFactory.getLogger(HomeController.class);

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;

    public HomeController(CourseService courseService,
                          CategoryService categoryService,
                          ReviewService reviewService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
    }

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "1") int page, Model model) {
        log.info("Request to view home page, page={}", page);
        int pageSize = 10;
        Page<Course> coursePage = courseService.fetchPublishedCourses(PageRequest.of(page - 1, pageSize));
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", categoryService.fetchAllCategories());
        model.addAttribute("recentReviews", reviewService.fetchRecentApprovedReviews());
        
        return "home";
    }
}
