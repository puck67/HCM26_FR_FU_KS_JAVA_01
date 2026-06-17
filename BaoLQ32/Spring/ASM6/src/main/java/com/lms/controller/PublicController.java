package com.lms.controller;

import com.lms.model.*;
import com.lms.service.LmsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PublicController {

    @Autowired
    private LmsService lmsService;

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return lmsService.getCategoryCloud();
    }

    @ModelAttribute("recentReviews")
    public List<Review> getRecentReviews() {
        return lmsService.getRecentApprovedReviews();
    }

    @GetMapping("/")
    public String home(
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Course> coursePage = lmsService.getRecentPublishedCourses(pageable);
        model.addAttribute("coursePage", coursePage);
        return "public/home";
    }

    @GetMapping("/courses/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = lmsService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));

        // Convert course markdown content to HTML
        String renderedContent = lmsService.renderMarkdown(course.getContent());
        model.addAttribute("course", course);
        model.addAttribute("renderedContent", renderedContent);

        // Get approved reviews
        List<Review> approvedReviews = lmsService.getApprovedReviewsForCourse(id);
        model.addAttribute("approvedReviews", approvedReviews);

        // Empty review for form
        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new Review());
        }
        return "public/details";
    }

    @PostMapping("/courses/{id}/reviews")
    public String submitReview(
            @PathVariable Long id,
            @Valid @ModelAttribute("review") Review review,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        Course course = lmsService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.review", bindingResult);
            redirectAttributes.addFlashAttribute("review", review);
            return "redirect:/courses/" + id + "#review-form";
        }

        review.setCourse(course);
        review.setStatus(1); // 1 = PENDING
        lmsService.saveReview(review);

        redirectAttributes.addFlashAttribute("successMessage", "Thank you! Your review has been submitted and is pending instructor approval.");
        return "redirect:/courses/" + id;
    }

    @GetMapping("/category/{name}")
    public String coursesByCategory(
            @PathVariable String name,
            @RequestParam(defaultValue = "0") int page,
            Model model
    ) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Course> coursePage = lmsService.getPublishedCoursesByCategory(name, pageable);
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("selectedCategory", name);
        return "public/category";
    }

    @GetMapping("/reviews/recent")
    public String recentReviewsPage(Model model) {
        List<Review> reviews = lmsService.getRecentApprovedReviews();
        model.addAttribute("recentReviewsList", reviews);
        return "public/reviews";
    }

    @GetMapping("/login")
    public String loginPage(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/instructor/dashboard";
        }
        return "public/login";
    }
}
