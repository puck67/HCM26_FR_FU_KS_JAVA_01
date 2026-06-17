package com.courses.controller;

import com.courses.entity.Course;
import com.courses.repository.CategoryRepository;
import com.courses.service.CourseService;
import com.courses.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructor")
@RequiredArgsConstructor
public class InstructorController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseService.countAll());
        model.addAttribute("publishedCourses", courseService.countPublished());
        model.addAttribute("pendingReviews", reviewService.countPending());
        model.addAttribute("totalCategories", categoryRepository.count());
        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String listCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "instructor/courses";
    }

    @GetMapping("/courses/create")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "instructor/course-form";
    }

    @GetMapping("/courses/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        return "instructor/course-form";
    }

    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute("course") Course course,
                             RedirectAttributes redirectAttributes) {
        if (course.getTitle() == null || course.getTitle().trim().isEmpty() ||
            course.getDescription() == null || course.getDescription().trim().isEmpty() ||
            course.getContent() == null || course.getContent().trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "All fields are required.");
            return "redirect:/instructor/courses/create";
        }

        try {
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course saved successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error saving course: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/publish/{id}")
    public String publishCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);
            course.setStatus(2); // Published
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course published successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/unpublish/{id}")
    public String unpublishCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);
            course.setStatus(1); // Draft
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course unpublished!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/archive/{id}")
    public String archiveCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Course course = courseService.getCourseById(id);
            course.setStatus(3); // Archived
            courseService.saveCourse(course);
            redirectAttributes.addFlashAttribute("successMessage", "Course archived successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/delete/{id}")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error deleting course: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(Model model) {
        model.addAttribute("pendingReviews", reviewService.getPendingReviews());
        model.addAttribute("approvedReviews", reviewService.getApprovedReviews());
        return "instructor/reviews";
    }

    @GetMapping("/reviews/approve/{id}")
    public String approveReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.approveReview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Review approved!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/instructor/reviews";
    }

    @GetMapping("/reviews/delete/{id}")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            reviewService.deleteReview(id);
            redirectAttributes.addFlashAttribute("successMessage", "Review deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error: " + e.getMessage());
        }
        return "redirect:/instructor/reviews";
    }
}
