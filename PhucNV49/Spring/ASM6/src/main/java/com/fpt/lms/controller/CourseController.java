package com.fpt.lms.controller;

import com.fpt.lms.entity.*;
import com.fpt.lms.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CourseController {

    @Autowired private CourseService courseService;
    @Autowired private ReviewService reviewService;
    @Autowired private CategoryService categoryService;
    @Autowired private MarkdownService markdownService;

    @GetMapping("/courses/{id}")
    public String courseDetail(@PathVariable Long id, Model model) {
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
            review.setStatus(1); // PENDING
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
