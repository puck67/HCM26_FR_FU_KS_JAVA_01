package com.fpt.lms.controller;

import com.fpt.lms.dto.CourseFormDTO;
import com.fpt.lms.entity.Course;
import com.fpt.lms.entity.CourseStatus;
import com.fpt.lms.entity.Instructor;
import com.fpt.lms.repository.CourseJdbcRepository;
import com.fpt.lms.repository.InstructorRepository;
import com.fpt.lms.service.CourseService;
import com.fpt.lms.service.ReviewService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for instructor-facing operations (course CRUD, review management).
 *
 * Improvements:
 * - Constructor injection (testable)
 * - Ownership check on course save — only owner can edit/delete their course
 * - Uses CourseFormDTO instead of binding to entity directly
 * - Uses CourseStatus enum instead of magic numbers
 * - SLF4J logging
 */
@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private static final Logger log = LoggerFactory.getLogger(InstructorController.class);

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

    // ── Helper ─────────────────────────────────────────────────────────────

    private Instructor currentInstructor(Authentication auth) {
        return instructorRepository.findByUsername(auth.getName())
            .orElseThrow(() -> new EntityNotFoundException("Instructor not found: " + auth.getName()));
    }

    // ── Dashboard ──────────────────────────────────────────────────────────

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseJdbcRepository.countAllCourses());
        model.addAttribute("publishedCourses", courseJdbcRepository.countPublishedCourses());
        model.addAttribute("pendingReviews", courseJdbcRepository.countPendingReviews());
        model.addAttribute("recentCourses", courseService.getAllCourses().stream().limit(5).toList());
        return "instructor/dashboard";
    }

    // ── Course Management ──────────────────────────────────────────────────

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "instructor/courses";
    }

    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("courseForm", new CourseFormDTO());
        model.addAttribute("editMode", false);
        return "instructor/course-form";
    }

    @PostMapping("/courses/save")
    public String saveCourse(@Valid @ModelAttribute("courseForm") CourseFormDTO dto,
                             BindingResult result,
                             Authentication auth,
                             RedirectAttributes attrs,
                             Model model) {
        if (result.hasErrors()) {
            model.addAttribute("editMode", dto.getId() != null);
            return "instructor/course-form";
        }

        Instructor instructor = currentInstructor(auth);

        // Ownership check — if editing existing course, verify ownership
        if (dto.getId() != null) {
            Course existing = courseService.findByIdOrThrow(dto.getId());
            if (existing.getInstructor() != null
                    && !existing.getInstructor().getUsername().equals(auth.getName())) {
                log.warn("Unauthorized course edit attempt: user={}, courseId={}", auth.getName(), dto.getId());
                attrs.addFlashAttribute("error", "You are not authorized to edit this course.");
                return "redirect:/instructor/courses";
            }
        }

        courseService.saveFromDTO(dto, instructor);
        attrs.addFlashAttribute("success", "Course saved successfully.");
        return "redirect:/instructor/courses";
    }

    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Authentication auth,
                                  Model model, RedirectAttributes attrs) {
        Course course = courseService.findByIdOrThrow(id);

        // Ownership check
        if (course.getInstructor() != null
                && !course.getInstructor().getUsername().equals(auth.getName())) {
            attrs.addFlashAttribute("error", "You are not authorized to edit this course.");
            return "redirect:/instructor/courses";
        }

        CourseFormDTO dto = CourseFormDTO.builder()
            .id(course.getId())
            .title(course.getTitle())
            .description(course.getDescription())
            .content(course.getContent())
            .category(course.getCategory())
            .status(course.getStatus())
            .build();

        model.addAttribute("courseForm", dto);
        model.addAttribute("editMode", true);
        return "instructor/course-form";
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id, Authentication auth, RedirectAttributes attrs) {
        Course course = courseService.findByIdOrThrow(id);

        // Ownership check
        if (course.getInstructor() != null
                && !course.getInstructor().getUsername().equals(auth.getName())) {
            log.warn("Unauthorized course delete attempt: user={}, courseId={}", auth.getName(), id);
            attrs.addFlashAttribute("error", "You are not authorized to delete this course.");
            return "redirect:/instructor/courses";
        }

        courseService.delete(id);
        attrs.addFlashAttribute("success", "Course deleted.");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/{id}/publish")
    public String publishCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.updateStatus(id, CourseStatus.PUBLISHED);
        attrs.addFlashAttribute("success", "Course published.");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/{id}/unpublish")
    public String unpublishCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.updateStatus(id, CourseStatus.DRAFT);
        attrs.addFlashAttribute("success", "Course set back to draft.");
        return "redirect:/instructor/courses";
    }

    @PostMapping("/courses/{id}/archive")
    public String archiveCourse(@PathVariable Long id, RedirectAttributes attrs) {
        courseService.updateStatus(id, CourseStatus.ARCHIVED);
        attrs.addFlashAttribute("success", "Course archived.");
        return "redirect:/instructor/courses";
    }

    // ── Review Management ──────────────────────────────────────────────────

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
