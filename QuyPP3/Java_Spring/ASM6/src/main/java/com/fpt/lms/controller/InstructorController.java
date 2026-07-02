package com.fpt.lms.controller;

import com.fpt.lms.entity.*;
import com.fpt.lms.repository.CourseJdbcRepository;
import com.fpt.lms.repository.InstructorRepository;
import com.fpt.lms.service.*;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final CourseService courseService;
    private final ReviewService reviewService;
    private final InstructorRepository instructorRepository;
    private final CourseJdbcRepository courseJdbcRepository;

    public InstructorController(CourseService courseService,
                                ReviewService reviewService,
                                InstructorRepository instructorRepository,
                                CourseJdbcRepository courseJdbcRepository) {
        this.courseService = courseService;
        this.reviewService = reviewService;
        this.instructorRepository = instructorRepository;
        this.courseJdbcRepository = courseJdbcRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseJdbcRepository.countAllCourses());
        model.addAttribute("publishedCourses", courseJdbcRepository.countPublishedCourses());
        model.addAttribute("pendingReviews", courseJdbcRepository.countPendingReviews());
        model.addAttribute("recentCourses", courseService.getAllCourses().stream().limit(5).toList());
        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "instructor/courses";
    }

    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("editMode", false);
        return "instructor/course-form";
    }

    @PostMapping("/courses/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult result,
                               Authentication auth,
                               RedirectAttributes attrs,
                               Model model) {
        if (result.hasErrors()) {
            model.addAttribute("editMode", course.getId() != null);
            return "instructor/course-form";
        }
        Instructor instructor = instructorRepository.findByUsername(auth.getName()).orElseThrow();
        course.setInstructor(instructor);
        if (course.getStatus() == null) course.setStatus(1);
        courseService.save(course);
        attrs.addFlashAttribute("success", "Course saved successfully.");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        return courseService.findById(id).map(course -> {
            model.addAttribute("course", course);
            model.addAttribute("editMode", true);
            return "instructor/course-form";
        }).orElse("redirect:/instructor/courses");
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.delete(id);
        attrs.addFlashAttribute("success", "Course deleted.");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/{id}/publish")
    public String publishCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.updateStatus(id, 2);
        attrs.addFlashAttribute("success", "Course published.");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/{id}/unpublish")
    public String unpublishCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.updateStatus(id, 1);
        attrs.addFlashAttribute("success", "Course unpublished.");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/{id}/archive")
    public String archiveCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.updateStatus(id, 3);
        attrs.addFlashAttribute("success", "Course archived.");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(Model model) {
        model.addAttribute("pendingReviews", reviewService.getPendingReviews());
        model.addAttribute("approvedReviews", reviewService.getApprovedReviews());
        return "instructor/reviews";
    }

    @PostMapping("/reviews/{id}/approve")
    public String approveReview(@PathVariable Long id, RedirectAttributes attrs) {
        reviewService.approve(id);
        attrs.addFlashAttribute("success", "Review approved.");
        return "redirect:/instructor/reviews";
    }

    @PostMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes attrs) {
        reviewService.delete(id);
        attrs.addFlashAttribute("success", "Review deleted.");
        return "redirect:/instructor/reviews";
    }
}

