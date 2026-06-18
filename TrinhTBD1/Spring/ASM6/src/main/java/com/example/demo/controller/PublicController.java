package com.example.demo.controller;

import com.example.demo.model.Course;
import com.example.demo.model.Review;
import com.example.demo.service.CourseService;
import com.example.demo.service.InstructorService;
import com.example.demo.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PublicController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final InstructorService instructorService;

    @GetMapping("/")
    public String homePage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Course> coursePage = courseService.getPublishedCourses(pageable);
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", courseService.getAllCategories());
        model.addAttribute("recentReviews", reviewService.getRecentApprovedReviews());
        return "public/home";
    }

    @GetMapping("/course/{id}")
    public String courseDetailsPage(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course.getStatus() != 2) {
            throw new IllegalArgumentException("Course is not published");
        }
        
        List<Review> approvedReviews = reviewService.getApprovedReviewsForCourse(id);
        
        model.addAttribute("course", course);
        model.addAttribute("reviews", approvedReviews);
        if (!model.containsAttribute("newReview")) {
            model.addAttribute("newReview", new Review());
        }
        return "public/course-details";
    }

    @PostMapping("/course/{courseId}/review")
    public String submitReview(
            @PathVariable("courseId") Long courseId,
            @Valid @ModelAttribute("newReview") Review review,
            BindingResult bindingResult,
            Model model) {
        
        Course course = courseService.getCourseById(courseId);
        if (course.getStatus() != 2) {
            throw new IllegalArgumentException("Cannot review an unpublished course");
        }

        if (bindingResult.hasErrors()) {
            List<Review> approvedReviews = reviewService.getApprovedReviewsForCourse(courseId);
            model.addAttribute("course", course);
            model.addAttribute("reviews", approvedReviews);
            model.addAttribute("newReview", review);
            model.addAttribute("org.springframework.validation.BindingResult.newReview", bindingResult);
            return "public/course-details";
        }

        review.setCourse(course);
        review.setStatus(1);
        reviewService.saveReview(review);

        return new StringBuilder()
                .append("redirect:/course/")
                .append(courseId)
                .append("?reviewSubmitted=true")
                .toString();
    }

    @GetMapping("/categories")
    public String categoriesPage(
            @RequestParam(name = "name", required = false) String categoryName,
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, 10);
        Page<Course> coursePage;
        
        if (categoryName != null && !categoryName.trim().isEmpty()) {
            coursePage = courseService.getCoursesByCategory(categoryName.trim(), pageable);
            model.addAttribute("selectedCategory", categoryName.trim());
        } else {
            coursePage = Page.empty();
        }
        
        model.addAttribute("coursePage", coursePage);
        model.addAttribute("categories", courseService.getAllCategories());
        return "public/categories";
    }

    @GetMapping("/reviews/recent")
    public String recentReviewsPage(
            @RequestParam(name = "page", defaultValue = "0") int page,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, 10);
        Page<Review> reviewPage = reviewService.getReviewsByStatus(2, pageable);
        
        model.addAttribute("reviewPage", reviewPage);
        return "public/recent-reviews";
    }

    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session != null && session.getAttribute("instructor") != null) {
            return "redirect:/instructor/dashboard";
        }
        return "public/login";
    }

    @PostMapping("/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            HttpServletRequest request,
            Model model) {
        
        boolean success = instructorService.login(username, password);
        if (success) {
            HttpSession session = request.getSession(true);
            session.setAttribute("instructor", username);
            return "redirect:/instructor/dashboard";
        }
        
        model.addAttribute("error", "Invalid username or password");
        model.addAttribute("username", username);
        return "public/login";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/login?logout=true";
    }
}
