package com.fpt.lms.controller;

import com.fpt.lms.entity.Course;
import com.fpt.lms.entity.Review;
import com.fpt.lms.service.CategoryService;
import com.fpt.lms.service.CourseService;
import com.fpt.lms.service.MarkdownService;
import com.fpt.lms.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Public-facing controller for course viewing and review submission.
 *
 * Improvements:
 * - Explicit imports (no wildcards)
 * - Constructor injection
 * - Uses findWithReviewsById() for efficient loading on course detail page
 */
@Controller
public class CourseController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;
    private final MarkdownService markdownService;

    public CourseController(CourseService courseService,
                            ReviewService reviewService,
                            CategoryService categoryService,
                            MarkdownService markdownService) {
        this.courseService = courseService;
        this.reviewService = reviewService;
        this.categoryService = categoryService;
        this.markdownService = markdownService;
    }


    @GetMapping("/courses/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
        // Use EntityGraph version to avoid N+1 on reviews
        return courseService.findById(id).map(course -> {
            model.addAttribute("course", course);
            model.addAttribute("contentHtml", markdownService.render(course.getContent()));
            model.addAttribute("reviews", reviewService.getApprovedReviews(id));
            model.addAttribute("newReview", new Review());
            model.addAttribute("categories", categoryService.getAllCategories());
            return "public/course-detail";
        }).orElse("redirect:/");
    }

    @PostMapping("/courses/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @Valid @ModelAttribute("newReview") Review review,
                               BindingResult result,
                               RedirectAttributes attrs) {
        if (result.hasErrors()) {
            attrs.addFlashAttribute("reviewError", "Please fill in all required fields correctly.");
            return "redirect:/courses/" + id;
        }
        courseService.findById(id).ifPresent(course -> {
            review.setCourse(course);
            review.setStatus(1); // PENDING — awaiting instructor approval
            reviewService.save(review);
        });
        attrs.addFlashAttribute("reviewSuccess", "Your review has been submitted and is awaiting approval.");
        return "redirect:/courses/" + id;
    }

    @GetMapping("/categories")
    public String categoryList(Model model) {
        model.addAttribute("categories", categoryService.getAllCategories());
        return "public/categories";
    }

    @GetMapping("/categories/{name}")
    public String coursesByCategory(@PathVariable String name, Model model) {
        model.addAttribute("courses", courseService.getCoursesByCategory(name));
        model.addAttribute("categoryName", name);
        model.addAttribute("categories", categoryService.getAllCategories());
        return "public/category-courses";
    }

    @GetMapping("/reviews")
    public String recentReviews(Model model) {
        model.addAttribute("reviews", reviewService.getRecentApprovedReviews());
        model.addAttribute("categories", categoryService.getAllCategories());
        return "public/recent-reviews";
    }
}
