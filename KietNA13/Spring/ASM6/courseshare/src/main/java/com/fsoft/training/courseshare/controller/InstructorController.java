package com.fsoft.training.courseshare.controller;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.entity.Review;
import com.fsoft.training.courseshare.service.CategoryService;
import com.fsoft.training.courseshare.service.CourseService;
import com.fsoft.training.courseshare.service.ReviewService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private static final Logger log = LoggerFactory.getLogger(InstructorController.class);

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final CategoryService categoryService;

    public InstructorController(CourseService courseService,
                                ReviewService reviewService,
                                CategoryService categoryService) {
        this.courseService = courseService;
        this.reviewService = reviewService;
        this.categoryService = categoryService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        log.info("Request to view instructor dashboard");
        model.addAttribute("totalCourses", courseService.countTotalCourses());
        model.addAttribute("publishedCourses", courseService.countPublishedCourses());
        model.addAttribute("pendingReviews", reviewService.countPendingReviews());
        model.addAttribute("totalCategories", categoryService.fetchAllCategories().size());
        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(@RequestParam(defaultValue = "1") int page, Model model) {
        log.info("Request to manage courses, page={}", page);
        int pageSize = 10;
        Page<Course> coursePage = courseService.fetchAllCourses(PageRequest.of(page - 1, pageSize));
        model.addAttribute("coursePage", coursePage);
        return "instructor/manage-courses";
    }

    @GetMapping("/course/new")
    public String createCourseForm(Model model) {
        log.info("Request to create course form");
        Course course = new Course();
        course.setStatus(CourseService.STATUS_DRAFT);
        model.addAttribute("course", course);
        return "instructor/course-form";
    }

    @GetMapping("/course/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        log.info("Request to edit course id={}", id);
        Course course = courseService.findCourseById(id);
        if (course == null) {
            log.warn("Course {} not found for edit", id);
            return "redirect:/instructor/courses";
        }
        model.addAttribute("course", course);
        return "instructor/course-form";
    }

    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        log.info("Request to save course: id={}, title={}", course.getId(), course.getTitle());
        courseService.saveCourseEntity(course);
        redirectAttributes.addFlashAttribute("message", "Course saved successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/course/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Request to delete course id={}", id);
        courseService.deleteCourseById(id);
        redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
        return "redirect:/instructor/courses";
    }
    
    @GetMapping("/course/{id}/status")
    public String updateCourseStatus(@PathVariable Long id, @RequestParam Integer status, RedirectAttributes redirectAttributes) {
        log.info("Request to update course {} status to {}", id, status);
        Course course = courseService.findCourseById(id);
        if (course != null) {
            course.setStatus(status);
            courseService.saveCourseEntity(course);
            redirectAttributes.addFlashAttribute("message", "Course status updated.");
            log.info("Course {} status updated successfully to {}", id, status);
        } else {
            log.warn("Course {} not found for status update", id);
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(@RequestParam(defaultValue = "1") int page, Model model) {
        log.info("Request to manage reviews, page={}", page);
        int pageSize = 10;
        Page<Review> pendingPage = reviewService.fetchPendingReviews(PageRequest.of(page - 1, pageSize));
        Page<Review> approvedPage = reviewService.fetchApprovedReviews(PageRequest.of(page - 1, pageSize));
        
        model.addAttribute("pendingPage", pendingPage);
        model.addAttribute("approvedPage", approvedPage);
        return "instructor/reviews";
    }

    @GetMapping("/review/{id}/approve")
    public String approveReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Request to approve review id={}", id);
        reviewService.approveReviewById(id);
        redirectAttributes.addFlashAttribute("message", "Review approved.");
        return "redirect:/instructor/reviews";
    }

    @GetMapping("/review/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        log.info("Request to delete review id={}", id);
        reviewService.deleteReviewById(id);
        redirectAttributes.addFlashAttribute("message", "Review deleted.");
        return "redirect:/instructor/reviews";
    }
}
