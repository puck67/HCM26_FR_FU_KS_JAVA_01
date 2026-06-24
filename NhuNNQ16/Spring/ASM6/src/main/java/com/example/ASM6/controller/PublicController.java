package com.example.ASM6.controller;

import com.example.ASM6.model.Category;
import com.example.ASM6.model.Course;
import com.example.ASM6.model.Review;
import com.example.ASM6.service.CategoryService;
import com.example.ASM6.service.CourseService;
import com.example.ASM6.service.LookupService;
import com.example.ASM6.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PublicController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final LookupService lookupService;

    // Inject categories into all models handled by this controller
    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categoryService.getAllCategories();
    }

    // Inject recent reviews into all models
    @ModelAttribute("recentReviews")
    public List<Review> getRecentReviews() {
        return reviewService.getRecentApprovedReviews(5);
    }

    // Helper for status lookup mapping
    @ModelAttribute("lookupService")
    public LookupService getLookupService() {
        return lookupService;
    }

    // Home Page - Published Courses with Pagination
    @GetMapping("/")
    public String home(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        Page<Course> coursePage = courseService.getPublishedCourses(page, 10);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("currentPage", page);
        return "public/home";
    }

    // Course Details Page - Display Course and its Approved Reviews
    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));
        
        // Ensure course is published, or allow viewing if instructor is logged in (session is optional here)
        if (course.getStatus() != 2) {
            // Check if user is instructor (just in case they are previewing)
            // But usually, public details page only displays published courses.
            return "redirect:/";
        }

        List<Review> approvedReviews = reviewService.getApprovedReviewsForCourse(course);
        model.addAttribute("course", course);
        model.addAttribute("reviews", approvedReviews);
        
        // Initialize an empty review for the "Write a Review" form
        if (!model.containsAttribute("newReview")) {
            Review review = new Review();
            review.setCourse(course);
            model.addAttribute("newReview", review);
        }
        
        return "public/course-details";
    }

    // Submit Review (defaults to status = 1 (PENDING))
    @PostMapping("/course/{id}/review")
    public String submitReview(
            @PathVariable("id") Long id,
            @Valid @ModelAttribute("newReview") Review review,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes) {
        
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.newReview", bindingResult);
            redirectAttributes.addFlashAttribute("newReview", review);
            return "redirect:/course/" + id + "#review-form";
        }

        review.setCourse(course);
        reviewService.createReview(review);
        
        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your review has been submitted and is pending approval by the instructor.");
        return "redirect:/course/" + id;
    }

    // Filter Courses by Category
    @GetMapping("/category/{name}")
    public String coursesByCategory(
            @PathVariable("name") String name,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        Page<Course> coursePage = courseService.getPublishedCoursesByCategory(name, page, 10);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("currentPage", page);
        model.addAttribute("selectedCategory", name);
        return "public/category-courses";
    }

    // Most Recent Reviews Page
    @GetMapping("/reviews")
    public String allRecentReviews(Model model) {
        List<Review> allReviews = reviewService.getRecentApprovedReviews(50); // Get top 50 recent reviews
        model.addAttribute("allReviews", allReviews);
        return "public/recent-reviews";
    }
}
