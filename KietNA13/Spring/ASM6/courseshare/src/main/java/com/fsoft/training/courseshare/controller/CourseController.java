package com.fsoft.training.courseshare.controller;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.entity.Review;
import com.fsoft.training.courseshare.service.CategoryService;
import com.fsoft.training.courseshare.service.CourseService;
import com.fsoft.training.courseshare.service.MarkdownService;
import com.fsoft.training.courseshare.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CourseController {

    private static final Logger log = LoggerFactory.getLogger(CourseController.class);

    private final CourseService courseService;
    private final CategoryService categoryService;
    private final ReviewService reviewService;
    private final MarkdownService markdownService;

    public CourseController(CourseService courseService,
                            CategoryService categoryService,
                            ReviewService reviewService,
                            MarkdownService markdownService) {
        this.courseService = courseService;
        this.categoryService = categoryService;
        this.reviewService = reviewService;
        this.markdownService = markdownService;
    }

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        log.info("Request to view details of course: {}", id);
        Course course = courseService.findCourseById(id);
        if (course == null || !course.getStatus().equals(CourseService.STATUS_PUBLISHED)) {
            log.warn("Course {} is not found or not published", id);
            return "redirect:/";
        }
        
        model.addAttribute("course", course);
        model.addAttribute("htmlContent", markdownService.convertMarkdownToHtml(course.getContent()));
        model.addAttribute("reviews", reviewService.fetchApprovedReviewsByCourse(id));
        model.addAttribute("newReview", new Review());
        
        return "course-details";
    }

    @PostMapping("/course/{id}/review")
    public String submitReview(@PathVariable Long id, @ModelAttribute Review review, RedirectAttributes redirectAttributes) {
        log.info("Request to submit review for course: {}", id);
        Course course = courseService.findCourseById(id);
        if (course != null) {
            review.setCourse(course);
            reviewService.saveReviewEntity(review);
            redirectAttributes.addFlashAttribute("message", "Review submitted successfully and is pending approval.");
            log.info("Review submitted successfully for course: {}", id);
        } else {
            log.warn("Failed to submit review. Course {} not found", id);
        }
        return "redirect:/course/" + id;
    }

    @GetMapping("/category/{name}")
    public String categoryList(@PathVariable String name, @RequestParam(defaultValue = "1") int page, Model model) {
        log.info("Request list courses in category '{}', page={}", name, page);
        int pageSize = 10;
        Page<Course> coursePage = courseService.fetchPublishedCoursesByCategory(name, PageRequest.of(page - 1, pageSize));
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categoryName", name);
        model.addAttribute("categories", categoryService.fetchAllCategories());
        
        return "category-list";
    }
}
