package com.fpt.lms.controller;

import com.fpt.lms.entity.*;
import com.fpt.lms.service.*;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    public String courseDetail(@PathVariable Long id,
                               @RequestParam(value = "reviewSuccess", required = false) Boolean reviewSuccess,
                               Model model) {
        return courseService.findById(id).map(course -> {
            model.addAttribute("course", course);
            model.addAttribute("contentHtml", markdownService.render(course.getContent()));
            model.addAttribute("reviews", reviewService.getApprovedReviews(id));
            if (!model.containsAttribute("newReview")) {
                model.addAttribute("newReview", new Review());
            }
            if (reviewSuccess != null && reviewSuccess) {
                model.addAttribute("reviewSuccess", "Your review has been submitted and is awaiting approval.");
            }
            model.addAttribute("categories", categoryService.getAllCategories());
            return "public/course-detail";
        }).orElse("redirect:/");
    }

    @PostMapping("/courses/{id}/review")
    public String submitReview(@PathVariable Long id,
                                 @Valid @ModelAttribute("newReview") Review review,
                                 BindingResult result,
                                 Model model) {
        if (result.hasErrors()) {
            return courseService.findById(id).map(course -> {
                model.addAttribute("course", course);
                model.addAttribute("contentHtml", markdownService.render(course.getContent()));
                model.addAttribute("reviews", reviewService.getApprovedReviews(id));
                model.addAttribute("categories", categoryService.getAllCategories());
                model.addAttribute("reviewError", "Please fill in all required fields correctly.");
                return "public/course-detail";
            }).orElse("redirect:/");
        }
        courseService.findById(id).ifPresent(course -> {
            review.setCourse(course);
            review.setStatus(1); // PENDING
            reviewService.save(review);
        });
        return "redirect:/courses/" + id + "?reviewSuccess=true";
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

