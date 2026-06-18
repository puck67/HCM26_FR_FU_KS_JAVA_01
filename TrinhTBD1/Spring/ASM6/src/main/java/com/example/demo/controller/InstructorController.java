package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Review;
import com.example.demo.service.CourseService;
import com.example.demo.service.LookupService;
import com.example.demo.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
@Slf4j
public class InstructorController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final LookupService lookupService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseService.countCoursesByStatus(null));
        model.addAttribute("publishedCourses", courseService.countCoursesByStatus(2));
        model.addAttribute("pendingReviews", reviewService.countReviewsByStatus(1));
        model.addAttribute("totalCategories", courseService.getAllCategories().size());
        return "instructor/dashboard";
    }

    @GetMapping("/reviews")
    public String manageReviews(
            @RequestParam(name = "pendingPage", defaultValue = "0") int pendingPage,
            @RequestParam(name = "approvedPage", defaultValue = "0") int approvedPage,
            Model model) {

        model.addAttribute("pendingPage",
                reviewService.getReviewsByStatus(1, PageRequest.of(pendingPage, 10)));
        model.addAttribute("approvedPage",
                reviewService.getReviewsByStatus(2, PageRequest.of(approvedPage, 10)));
        return "instructor/manage-reviews";
    }

    @GetMapping("/reviews/approve/{id}")
    public String approveReview(@PathVariable("id") Long id) {
        reviewService.approveReview(id);
        return "redirect:/instructor/reviews?approved=true";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return "redirect:/instructor/reviews?deleted=true";
    }
}
