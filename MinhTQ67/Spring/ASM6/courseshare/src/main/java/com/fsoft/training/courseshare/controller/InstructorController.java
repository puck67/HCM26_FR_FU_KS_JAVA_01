package com.fsoft.training.courseshare.controller;

import com.fsoft.training.courseshare.entity.Course;
import com.fsoft.training.courseshare.entity.Review;
import com.fsoft.training.courseshare.service.CategoryService;
import com.fsoft.training.courseshare.service.CourseService;
import com.fsoft.training.courseshare.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseService.getTotalCourses());
        model.addAttribute("publishedCourses", courseService.getPublishedCoursesCount());
        model.addAttribute("pendingReviews", reviewService.getPendingReviewsCount());
        model.addAttribute("totalCategories", categoryService.getAllCategories().size());
        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        Page<Course> coursePage = courseService.getAllCourses(PageRequest.of(page - 1, pageSize));
        model.addAttribute("coursePage", coursePage);
        return "instructor/manage-courses";
    }

    @GetMapping("/course/new")
    public String createCourseForm(Model model) {
        Course course = new Course();
        course.setStatus(CourseService.STATUS_DRAFT);
        model.addAttribute("course", course);
        return "instructor/course-form";
    }

    @GetMapping("/course/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) return "redirect:/instructor/courses";
        model.addAttribute("course", course);
        return "instructor/course-form";
    }

    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course, RedirectAttributes redirectAttributes) {
        courseService.saveCourse(course);
        redirectAttributes.addFlashAttribute("message", "Course saved successfully!");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/course/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        courseService.deleteCourse(id);
        redirectAttributes.addFlashAttribute("message", "Course deleted successfully!");
        return "redirect:/instructor/courses";
    }
    
    @GetMapping("/course/{id}/status")
    public String updateCourseStatus(@PathVariable Long id, @RequestParam Integer status, RedirectAttributes redirectAttributes) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            course.setStatus(status);
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("message", "Course status updated.");
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(@RequestParam(defaultValue = "1") int page, Model model) {
        int pageSize = 10;
        Page<Review> pendingPage = reviewService.getPendingReviews(PageRequest.of(page - 1, pageSize));
        Page<Review> approvedPage = reviewService.getApprovedReviews(PageRequest.of(page - 1, pageSize));
        
        model.addAttribute("pendingPage", pendingPage);
        model.addAttribute("approvedPage", approvedPage);
        return "instructor/reviews";
    }

    @GetMapping("/review/{id}/approve")
    public String approveReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.approveReview(id);
        redirectAttributes.addFlashAttribute("message", "Review approved.");
        return "redirect:/instructor/reviews";
    }

    @GetMapping("/review/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.deleteReview(id);
        redirectAttributes.addFlashAttribute("message", "Review deleted.");
        return "redirect:/instructor/reviews";
    }
}
