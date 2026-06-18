package com.fresheracademy.lms.controller;

import com.fresheracademy.lms.entity.Course;
import com.fresheracademy.lms.entity.Review;
import com.fresheracademy.lms.service.CourseService;
import com.fresheracademy.lms.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class PublicController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private com.fresheracademy.lms.repository.CategoryRepository categoryRepository;

    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = courseService.getPublishedCourses(page, 10);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("recentReviews", reviewService.getRecentApprovedReviews());
        return "public/home";
    }

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("review", new Review());
        return "public/course_details";
    }

    @PostMapping("/course/{id}/review")
    public String submitReview(@PathVariable Long id, @ModelAttribute Review review) {
        Course course = courseService.getCourseById(id);
        review.setCourse(course);
        reviewService.saveReview(review);
        return "redirect:/course/" + id + "?reviewSubmitted=true";
    }

    @GetMapping("/categories")
    public String categories(@RequestParam(required = false) String name, @RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("selectedCategory", name);
        if (name != null && !name.trim().isEmpty()) {
            Page<Course> coursePage = courseService.getCoursesByCategory(name.trim(), page, 10);
            model.addAttribute("courses", coursePage.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", coursePage.getTotalPages());
        }
        return "public/categories";
    }

    @GetMapping("/recent-reviews")
    public String recentReviews(Model model) {
        model.addAttribute("reviews", reviewService.getRecentApprovedReviews());
        return "public/recent_reviews";
    }
}
