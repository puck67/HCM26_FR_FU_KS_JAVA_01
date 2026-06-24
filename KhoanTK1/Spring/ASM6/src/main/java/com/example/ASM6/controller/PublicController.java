package com.example.ASM6.controller;

import com.example.ASM6.model.Category;
import com.example.ASM6.model.Course;
import com.example.ASM6.model.Review;
import com.example.ASM6.service.CategoryService;
import com.example.ASM6.service.CourseService;
import com.example.ASM6.service.LookupService;
import com.example.ASM6.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;

@Controller
public class PublicController {

    private final CourseService courseSrv;
    private final ReviewService reviewSrv;
    private final CategoryService categorySrv;
    private final LookupService lookupSrv;

    public PublicController(CourseService courseSrv, ReviewService reviewSrv,
                            CategoryService categorySrv, LookupService lookupSrv) {
        this.courseSrv = courseSrv;
        this.reviewSrv = reviewSrv;
        this.categorySrv = categorySrv;
        this.lookupSrv = lookupSrv;
    }

    @ModelAttribute("categories")
    public List<Category> getCategories() {
        return categorySrv.getAllCategories();
    }

    @ModelAttribute("recentReviews")
    public List<Review> getRecentReviews() {
        return reviewSrv.getRecentApprovedReviews(5);
    }

    @ModelAttribute("lookupService")
    public LookupService getLookupService() {
        return lookupSrv;
    }

    @GetMapping("/")
    public String home(@RequestParam(name = "page", defaultValue = "0") int pageNum, Model m) {
        Page<Course> pg = courseSrv.getPublishedCourses(pageNum, 10);
        m.addAttribute("coursePage", pg);
        m.addAttribute("currentPage", pageNum);
        return "public/home";
    }

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable("id") Long courseId, Model m) {
        Course c = courseSrv.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId));
        
        if (c.getStatus() != 2) {
            return "redirect:/";
        }

        List<Review> reviewsList = reviewSrv.getApprovedReviewsForCourse(c);
        m.addAttribute("course", c);
        m.addAttribute("reviews", reviewsList);
        
        if (!m.containsAttribute("newReview")) {
            Review reviewObj = new Review();
            reviewObj.setCourse(c);
            m.addAttribute("newReview", reviewObj);
        }
        
        return "public/course-details";
    }

    @PostMapping("/course/{id}/review")
    public String submitReview(
            @PathVariable("id") Long courseId,
            @Valid @ModelAttribute("newReview") Review reviewObj,
            BindingResult result,
            RedirectAttributes ra) {
        
        Course c = courseSrv.getCourseById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + courseId));

        if (result.hasErrors()) {
            ra.addFlashAttribute("org.springframework.validation.BindingResult.newReview", result);
            ra.addFlashAttribute("newReview", reviewObj);
            return "redirect:/course/" + courseId + "#review-form";
        }

        reviewObj.setCourse(c);
        reviewSrv.createReview(reviewObj);
        
        ra.addFlashAttribute("successMessage", "Thank you! Your review has been submitted and is pending approval by the instructor.");
        return "redirect:/course/" + courseId;
    }

    @GetMapping("/category/{name}")
    public String coursesByCategory(
            @PathVariable("name") String catName,
            @RequestParam(name = "page", defaultValue = "0") int pageNum,
            Model m) {
        Page<Course> pg = courseSrv.getPublishedCoursesByCategory(catName, pageNum, 10);
        m.addAttribute("coursePage", pg);
        m.addAttribute("currentPage", pageNum);
        m.addAttribute("selectedCategory", catName);
        return "public/category-courses";
    }

    @GetMapping("/reviews")
    public String allRecentReviews(Model m) {
        List<Review> list = reviewSrv.getRecentApprovedReviews(50);
        m.addAttribute("allReviews", list);
        return "public/recent-reviews";
    }
}
