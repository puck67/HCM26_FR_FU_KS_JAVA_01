package com.fsoft.training.courseshare.controller;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.entity.Review;
import com.fsoft.training.courseshare.service.CategoryService;
import com.fsoft.training.courseshare.service.CourseService;
import com.fsoft.training.courseshare.service.MarkdownService;
import com.fsoft.training.courseshare.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private MarkdownService markdownService;

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null || course.getStatus() != CourseService.STATUS_PUBLISHED) {
            return "redirect:/";
        }
        
        model.addAttribute("course", course);
        model.addAttribute("htmlContent", markdownService.renderToHtml(course.getContent()));
        model.addAttribute("reviews", reviewService.getApprovedReviewsForCourse(id));
        model.addAttribute("newReview", new Review());
        
        return "course-details";
    }

    @PostMapping("/course/{id}/review")
    public String submitReview(@PathVariable Long id, @ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            review.setCourse(course);
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("message", "Review submitted successfully and is pending approval.");
        }
        return "redirect:/course/" + id;
    }

    @GetMapping("/category/{name}")
    public String categoryList(@PathVariable String name, @RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        Page<Course> coursePage = courseService.getPublishedCoursesByCategory(name, PageRequest.of(page - 1, pageSize));
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categoryName", name);
        model.addAttribute("categories", categoryService.getAllCategories());
        
        return "category-list";
    }
}
