package com.lms.controller;

import com.lms.model.Course;
import com.lms.model.Review;
import com.lms.service.LmsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private LmsService lmsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Course> courses = lmsService.getAllCourses();
        List<Review> reviews = lmsService.getAllReviews();

        long totalCourses = courses.size();
        long publishedCourses = courses.stream().filter(c -> c.getStatus() == 2).count();
        long draftCourses = courses.stream().filter(c -> c.getStatus() == 1).count();
        long archivedCourses = courses.stream().filter(c -> c.getStatus() == 3).count();

        long totalReviews = reviews.size();
        long pendingReviews = reviews.stream().filter(r -> r.getStatus() == 1).count();
        long approvedReviews = reviews.stream().filter(r -> r.getStatus() == 2).count();

        double avgRating = reviews.stream()
                .filter(r -> r.getStatus() == 2)
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("publishedCourses", publishedCourses);
        model.addAttribute("draftCourses", draftCourses);
        model.addAttribute("archivedCourses", archivedCourses);
        model.addAttribute("totalReviews", totalReviews);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("approvedReviews", approvedReviews);
        model.addAttribute("avgRating", String.format("%.2f", avgRating));

        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", lmsService.getAllCourses());
        return "instructor/courses";
    }

    @GetMapping("/courses/new")
    public String createCourseForm(Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        return "instructor/course_form";
    }

    @PostMapping("/courses/new")
    public String saveNewCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", bindingResult);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/instructor/courses/new";
        }
        lmsService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course created successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = lmsService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));
        model.addAttribute("course", course);
        return "instructor/course_form";
    }

    @PostMapping("/courses/edit/{id}")
    public String updateCourse(
            @PathVariable Long id,
            @Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "instructor/course_form";
        }
        Course existing = lmsService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));

        existing.setTitle(course.getTitle());
        existing.setDescription(course.getDescription());
        existing.setContent(course.getContent());
        existing.setStatus(course.getStatus());
        existing.setCategory(course.getCategory());

        lmsService.saveCourse(existing);
        redirectAttributes.addFlashAttribute("successMessage", "Course updated successfully!");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        lmsService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/status/{id}")
    public String updateCourseStatus(
            @PathVariable Long id,
            @RequestParam Integer status,
            RedirectAttributes redirectAttributes
    ) {
        Course course = lmsService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));
        course.setStatus(status);
        lmsService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course status updated successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(Model model) {
        model.addAttribute("reviews", lmsService.getAllReviews());
        return "instructor/reviews";
    }

    @PostMapping("/reviews/approve/{id}")
    public String approveReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Review review = lmsService.getReviewById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid review ID: " + id));
        review.setStatus(2); // 2 = APPROVED
        lmsService.saveReview(review);
        redirectAttributes.addFlashAttribute("successMessage", "Review approved successfully!");
        return "redirect:/instructor/reviews";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        lmsService.deleteReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully!");
        return "redirect:/instructor/reviews";
    }
}
