package com.fresheracademy.lms.controller;

import com.fresheracademy.lms.entity.Course;
import com.fresheracademy.lms.service.CourseService;
import com.fresheracademy.lms.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private com.fresheracademy.lms.repository.CategoryRepository categoryRepository;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("totalCourses", courseService.getAllCourses().size());
        long publishedCount = courseService.getAllCourses().stream().filter(c -> c.getStatus() == 2).count();
        model.addAttribute("publishedCourses", publishedCount);
        model.addAttribute("pendingReviews", reviewService.getPendingReviews().size());
        model.addAttribute("totalCategories", categoryRepository.count());
        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(Model model) {
        model.addAttribute("courses", courseService.getAllCourses());
        return "instructor/manage_courses";
    }

    @GetMapping("/course/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "instructor/course_form";
    }

    @PostMapping("/course/save")
    public String saveCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course);
        return "redirect:/instructor/courses";
    }

    @GetMapping("/course/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        if (course == null) return "redirect:/instructor/courses";
        model.addAttribute("course", course);
        return "instructor/course_form";
    }

    @PostMapping("/course/delete/{id}")
    public String deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return "redirect:/instructor/courses";
    }

    @PostMapping("/course/publish/{id}")
    public String publishCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            course.setStatus(2);
            courseService.saveCourse(course);
        }
        return "redirect:/instructor/courses";
    }

    @PostMapping("/course/unpublish/{id}")
    public String unpublishCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            course.setStatus(1);
            courseService.saveCourse(course);
        }
        return "redirect:/instructor/courses";
    }

    @PostMapping("/course/archive/{id}")
    public String archiveCourse(@PathVariable Long id) {
        Course course = courseService.getCourseById(id);
        if (course != null) {
            course.setStatus(3);
            courseService.saveCourse(course);
        }
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(Model model) {
        model.addAttribute("pendingReviews", reviewService.getPendingReviews());
        model.addAttribute("approvedReviews", reviewService.getApprovedReviews());
        return "instructor/manage_reviews";
    }

    @PostMapping("/review/approve/{id}")
    public String approveReview(@PathVariable Long id) {
        reviewService.approveReview(id);
        return "redirect:/instructor/reviews";
    }
    
    @PostMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable Long id) {
        reviewService.deleteReview(id);
        return "redirect:/instructor/reviews";
    }
}
