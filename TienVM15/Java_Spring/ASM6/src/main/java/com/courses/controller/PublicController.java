package com.courses.controller;

import com.courses.entity.Course;
import com.courses.entity.Review;
import com.courses.repository.CategoryRepository;
import com.courses.service.CourseService;
import com.courses.service.MarkdownService;
import com.courses.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final CategoryRepository categoryRepository;
    private final MarkdownService markdownService;

    // Home Page (list of recent published courses, pageable max 10)
    @GetMapping("/")
    public String home(Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Course> coursePage = courseService.getPublishedCourses(PageRequest.of(page, 10));
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", categoryRepository.findAllByOrderByFrequencyDesc());
        model.addAttribute("recentReviews", reviewService.getRecentApprovedReviews());
        return "home";
    }

    // Course Detail Page
    @GetMapping("/courses/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        
        // Parse markdown description to HTML
        String htmlContent = markdownService.parseToHtml(course.getContent());
        model.addAttribute("course", course);
        model.addAttribute("htmlContent", htmlContent);
        
        // Load approved reviews
        List<Review> approvedReviews = reviewService.getApprovedReviewsForCourse(id);
        model.addAttribute("reviews", approvedReviews);
        
        // Create new empty review for form
        model.addAttribute("newReview", new Review());
        return "course-details";
    }

    // Submit Review Page
    @PostMapping("/courses/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @ModelAttribute("newReview") Review review,
                               RedirectAttributes redirectAttributes) {
        if (review.getAuthorName() == null || review.getAuthorName().trim().isEmpty() ||
            review.getEmail() == null || review.getEmail().trim().isEmpty() ||
            review.getContent() == null || review.getContent().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "All review fields are required.");
            return "redirect:/courses/" + id;
        }

        try {
            Course course = courseService.getCourseById(id);
            review.setCourse(course);
            review.setStatus(1); // 1 = PENDING (requires instructor approval)
            reviewService.saveReview(review);
            redirectAttributes.addFlashAttribute("successMessage", "Review submitted! Waiting for instructor approval.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/courses/" + id;
    }

    // Categories filter page
    @GetMapping("/categories/{name}")
    public String categoryCourses(@PathVariable String name, Model model, @RequestParam(defaultValue = "0") int page) {
        Page<Course> coursePage = courseService.getPublishedCoursesByCategory(name, PageRequest.of(page, 10));
        model.addAttribute("categoryName", name);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", categoryRepository.findAllByOrderByFrequencyDesc());
        return "category-courses";
    }

    // Reviews list page
    @GetMapping("/reviews")
    public String reviewsList(Model model) {
        model.addAttribute("reviews", reviewService.getRecentApprovedReviews());
        return "reviews";
    }

    // Login page
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
