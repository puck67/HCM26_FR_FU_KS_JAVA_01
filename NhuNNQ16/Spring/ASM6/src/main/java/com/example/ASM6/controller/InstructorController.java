package com.example.ASM6.controller;

import com.example.ASM6.model.Course;
import com.example.ASM6.model.Instructor;
import com.example.ASM6.model.Review;
import com.example.ASM6.service.CourseService;
import com.example.ASM6.service.InstructorService;
import com.example.ASM6.service.LookupService;
import com.example.ASM6.service.ReviewService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class InstructorController {

    private final InstructorService instructorService;
    private final CourseService courseService;
    private final ReviewService reviewService;
    private final LookupService lookupService;

    // Helper for status lookup mapping
    @ModelAttribute("lookupService")
    public LookupService getLookupService() {
        return lookupService;
    }

    // --- Authentication ---

    @GetMapping("/instructor/login")
    public String showLoginForm(
            @RequestParam(name = "redirect", required = false) String redirect,
            HttpSession session,
            Model model) {
        // Redirect to dashboard if already logged in
        if (session.getAttribute("instructor") != null) {
            return "redirect:/instructor/dashboard";
        }
        model.addAttribute("redirect", redirect);
        return "instructor/login";
    }

    @PostMapping("/instructor/login")
    public String login(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam(name = "redirect", required = false) String redirect,
            HttpSession session,
            RedirectAttributes redirectAttributes) {
        
        Optional<Instructor> instructorOpt = instructorService.login(username, password);
        
        if (instructorOpt.isPresent()) {
            session.setAttribute("instructor", instructorOpt.get());
            if (redirect != null && !redirect.isEmpty()) {
                return "redirect:" + redirect;
            }
            return "redirect:/instructor/dashboard";
        } else {
            redirectAttributes.addFlashAttribute("error", "Invalid username or password");
            return "redirect:/instructor/login" + (redirect != null ? "?redirect=" + redirect : "");
        }
    }

    @GetMapping("/instructor/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // --- Instructor Dashboard ---

    @GetMapping("/instructor/dashboard")
    public String dashboard(Model model) {
        // Collect stats
        model.addAttribute("totalCourses", courseService.countAllCourses());
        model.addAttribute("draftCourses", courseService.countCoursesByStatus(1));
        model.addAttribute("publishedCourses", courseService.countCoursesByStatus(2));
        model.addAttribute("archivedCourses", courseService.countCoursesByStatus(3));

        model.addAttribute("totalReviews", reviewService.countAllReviews());
        model.addAttribute("pendingReviewsCount", reviewService.countReviewsByStatus(1));
        model.addAttribute("approvedReviewsCount", reviewService.countReviewsByStatus(2));

        // Show pending reviews for quick moderation on the dashboard
        List<Review> pendingReviews = reviewService.getReviewsByStatus(1);
        model.addAttribute("pendingReviews", pendingReviews);

        return "instructor/dashboard";
    }

    // --- Course Management ---

    @GetMapping("/instructor/courses")
    public String listCourses(Model model) {
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return "instructor/courses";
    }

    @GetMapping("/instructor/courses/new")
    public String showCreateForm(Model model) {
        if (!model.containsAttribute("course")) {
            Course course = new Course();
            course.setStatus(1); // default is DRAFT
            model.addAttribute("course", course);
        }
        model.addAttribute("isEdit", false);
        return "instructor/course-form";
    }

    @GetMapping("/instructor/courses/edit/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Course course = courseService.getCourseById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid course ID: " + id));
        model.addAttribute("course", course);
        model.addAttribute("isEdit", true);
        return "instructor/course-form";
    }

    @PostMapping("/instructor/courses/save")
    public String saveCourse(
            @Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", course.getId() != null);
            return "instructor/course-form";
        }

        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Course saved successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/courses/delete/{id}")
    public String deleteCourse(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/instructor/courses/status/{id}")
    public String changeStatus(
            @PathVariable("id") Long id,
            @RequestParam("status") Integer status,
            RedirectAttributes redirectAttributes) {
        
        courseService.updateStatus(id, status);
        String statusText = lookupService.getValue("COURSE_STATUS", String.valueOf(status));
        redirectAttributes.addFlashAttribute("successMessage", "Course status updated to " + statusText + "!");
        return "redirect:/instructor/courses";
    }

    // --- Review Moderation ---

    @GetMapping("/instructor/reviews")
    public String listReviews(Model model) {
        List<Review> pendingReviews = reviewService.getReviewsByStatus(1);
        List<Review> approvedReviews = reviewService.getReviewsByStatus(2);
        
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("approvedReviews", approvedReviews);
        
        return "instructor/reviews";
    }

    @GetMapping("/instructor/reviews/approve/{id}")
    public String approveReview(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        reviewService.approveReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Review approved successfully!");
        return "redirect:/instructor/reviews";
    }

    @GetMapping("/instructor/reviews/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        reviewService.deleteReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully!");
        return "redirect:/instructor/reviews";
    }
}
