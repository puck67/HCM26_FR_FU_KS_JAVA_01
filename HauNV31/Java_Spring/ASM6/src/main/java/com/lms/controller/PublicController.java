package com.lms.controller;

import com.lms.model.Course;
import com.lms.model.Review;
import com.lms.service.LmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class PublicController {

    @Autowired
    private LmsService lmsService;

    @GetMapping("/")
    public String index(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = lmsService.getPublishedCourses(page, 10);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("categories", lmsService.getCategories());
        model.addAttribute("recentReviews", lmsService.getRecentReviews());
        return "index";
    }

    @GetMapping("/courses/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        return Optional.ofNullable(lmsService.getCourseById(id))
                .filter(course -> course.getStatus() == 2)
                .map(course -> {
                    model.addAttribute("course", course);
                    model.addAttribute("reviews", lmsService.getReviewsByCourseAndStatus(id, 2));
                    if (!model.containsAttribute("review")) {
                        model.addAttribute("review", new Review());
                    }
                    return "course_details";
                })
                .orElse("redirect:/");
    }

    @PostMapping("/courses/{id}/review")
    public String submitReview(@PathVariable Long id,
                               @Valid @ModelAttribute("review") Review review,
                               BindingResult result,
                               Model model) {
        Course course = lmsService.getCourseById(id);
        if (course == null || course.getStatus() != 2) {
            return "redirect:/";
        }

        if (result.hasErrors()) {
            model.addAttribute("course", course);
            model.addAttribute("reviews", lmsService.getReviewsByCourseAndStatus(id, 2));
            return "course_details";
        }

        review.setStatus(1);
        review.setCourse(course);
        lmsService.saveReview(review);

        StringBuilder sb = new StringBuilder("redirect:/courses/");
        sb.append(id).append("?success=true");
        return sb.toString();
    }

    @GetMapping("/courses/category/{category}")
    public String categoryList(@PathVariable String category, Model model) {
        List<Course> courses = lmsService.getPublishedCoursesByCategory(category);
        model.addAttribute("courses", courses);
        model.addAttribute("categoryName", category);
        return "category_list";
    }

    @GetMapping("/instructor/login")
    public String showLogin(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("instructor") != null) {
            return "redirect:/instructor/dashboard";
        }
        return "login";
    }

    @PostMapping("/instructor/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpServletRequest request,
                        Model model) {
        if (lmsService.authenticateInstructor(username, password)) {
            HttpSession session = request.getSession(true);
            session.setAttribute("instructor", username);
            return "redirect:/instructor/dashboard";
        }
        model.addAttribute("error", "Invalid username or password");
        return "login";
    }

    @GetMapping("/instructor/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return "redirect:/instructor/login";
    }
}
