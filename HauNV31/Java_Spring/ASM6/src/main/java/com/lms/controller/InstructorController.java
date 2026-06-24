package com.lms.controller;

import com.lms.model.Course;
import com.lms.model.Review;
import com.lms.service.LmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private LmsService lmsService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<Course> courses = lmsService.getAllCourses();
        List<Review> reviews = lmsService.getAllReviews();

        long pendingReviewsCount = reviews.stream()
                .filter(r -> r.getStatus() == 1)
                .count();

        model.addAttribute("coursesCount", courses.size());
        model.addAttribute("reviewsCount", reviews.size());
        model.addAttribute("pendingReviewsCount", pendingReviewsCount);
        return "dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", lmsService.getAllCourses());
        return "manage_courses";
    }

    @GetMapping("/courses/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/courses/new")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult result) {
        if (result.hasErrors()) {
            return "create_course";
        }
        lmsService.saveCourse(course);
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/edit/{id}")
    public String showEditCourseForm(@PathVariable Long id, Model model) {
        return Optional.ofNullable(lmsService.getCourseById(id))
                .map(course -> {
                    model.addAttribute("course", course);
                    return "edit_course";
                })
                .orElse("redirect:/instructor/courses");
    }

    @PostMapping("/courses/edit/{id}")
    public String editCourse(@PathVariable Long id,
                             @Valid @ModelAttribute("course") Course course,
                             BindingResult result) {
        if (result.hasErrors()) {
            return "edit_course";
        }
        course.setId(id);
        lmsService.saveCourse(course);
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        lmsService.deleteCourse(id);
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/publish/{id}")
    public String publishCourse(@PathVariable Long id) {
        Optional.ofNullable(lmsService.getCourseById(id))
                .ifPresent(course -> {
                    course.setStatus(2);
                    lmsService.saveCourse(course);
                });
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/unpublish/{id}")
    public String unpublishCourse(@PathVariable Long id) {
        Optional.ofNullable(lmsService.getCourseById(id))
                .ifPresent(course -> {
                    course.setStatus(1);
                    lmsService.saveCourse(course);
                });
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/archive/{id}")
    public String archiveCourse(@PathVariable Long id) {
        Optional.ofNullable(lmsService.getCourseById(id))
                .ifPresent(course -> {
                    course.setStatus(3);
                    lmsService.saveCourse(course);
                });
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(Model model) {
        model.addAttribute("reviews", lmsService.getAllReviews());
        return "manage_reviews";
    }

    @PostMapping("/reviews/approve/{id}")
    public String approveReview(@PathVariable Long id) {
        lmsService.approveReview(id);
        return "redirect:/instructor/reviews";
    }

    @PostMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        lmsService.deleteReview(id);
        return "redirect:/instructor/reviews";
    }
}
