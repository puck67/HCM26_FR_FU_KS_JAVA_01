package com.example.asm6.controller;

import com.example.asm6.model.Course;
import com.example.asm6.service.CourseService;
import com.example.asm6.service.LookupService;
import com.example.asm6.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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
    private LookupService lookupService;

    // 1. QUẢN LÝ KHÓA HỌC: XEM DANH SÁCH KHÓA HỌC
    @GetMapping("/courses")
    public String listCourses(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = courseService.getAllCourses(page, 15);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("lookupService", lookupService); // Để hiển thị text trạng thái
        model.addAttribute("activePage", "instructor-courses");
        return "instructor/courses";
    }

    // 2. THÊM MỚI KHÓA HỌC: FORM
    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        if (!model.containsAttribute("course")) {
            model.addAttribute("course", new Course());
        }
        model.addAttribute("isNew", true);
        model.addAttribute("activePage", "instructor-courses");
        return "instructor/course-form";
    }

    // 3. THÊM MỚI KHÓA HỌC: SAVE
    @PostMapping("/courses")
    public String createCourse(@Valid @ModelAttribute("course") Course course,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/instructor/courses/new";
        }
        courseService.createCourse(course);
        redirectAttributes.addFlashAttribute("successMessage", "Khóa học đã được tạo thành công!");
        return "redirect:/instructor/courses";
    }

    // 4. CHỈNH SỬA KHÓA HỌC: FORM
    @GetMapping("/courses/{id}/edit")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        model.addAttribute("course", course);
        model.addAttribute("isNew", false);
        model.addAttribute("activePage", "instructor-courses");
        return "instructor/course-form";
    }

    // 5. CHỈNH SỬA KHÓA HỌC: UPDATE
    @PostMapping("/courses/{id}")
    public String updateCourse(@PathVariable Long id,
                               @Valid @ModelAttribute("course") Course course,
                               BindingResult result,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.course", result);
            redirectAttributes.addFlashAttribute("course", course);
            return "redirect:/instructor/courses/" + id + "/edit";
        }
        courseService.updateCourse(id, course);
        redirectAttributes.addFlashAttribute("successMessage", "Khóa học đã được cập nhật thành công!");
        return "redirect:/instructor/courses";
    }

    // 6. XÓA KHÓA HỌC
    @GetMapping("/courses/{id}/delete")
    public String deleteCourse(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            courseService.deleteCourse(id);
            redirectAttributes.addFlashAttribute("successMessage", "Khóa học đã được xóa thành công!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Không thể xóa khóa học. Lỗi: " + e.getMessage());
        }
        return "redirect:/instructor/courses";
    }

    // 7. THAY ĐỔI NHANH TRẠNG THÁI KHÓA HỌC (Publish, Unpublish, Archive)
    @GetMapping("/courses/{id}/status/{status}")
    public String changeCourseStatus(@PathVariable Long id,
                                     @PathVariable Integer status,
                                     RedirectAttributes redirectAttributes) {
        courseService.changeCourseStatus(id, status);
        String statusText = lookupService.getValue("COURSE_STATUS", String.valueOf(status));
        redirectAttributes.addFlashAttribute("successMessage", "Đã chuyển trạng thái khóa học sang: " + statusText);
        return "redirect:/instructor/courses";
    }

    // 8. QUẢN LÝ REVIEWS: XEM DANH SÁCH REVIEWS
    @GetMapping("/reviews")
    public String listReviews(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("reviewsPage", reviewService.getAllReviews(page, 15));
        model.addAttribute("lookupService", lookupService);
        model.addAttribute("activePage", "instructor-reviews");
        return "instructor/reviews";
    }

    // 9. QUẢN LÝ REVIEWS: PHÊ DUYỆT REVIEW
    @GetMapping("/reviews/{id}/approve")
    public String approveReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.approveReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã phê duyệt đánh giá!");
        return "redirect:/instructor/reviews";
    }

    // 10. QUẢN LÝ REVIEWS: XÓA REVIEW
    @GetMapping("/reviews/{id}/delete")
    public String deleteReview(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        reviewService.deleteReview(id);
        redirectAttributes.addFlashAttribute("successMessage", "Đã xóa đánh giá!");
        return "redirect:/instructor/reviews";
    }
}
