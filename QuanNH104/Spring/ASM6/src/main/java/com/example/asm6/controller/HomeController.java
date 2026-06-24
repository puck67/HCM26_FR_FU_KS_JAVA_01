package com.example.asm6.controller;

import com.example.asm6.model.Course;
import com.example.asm6.model.Review;
import com.example.asm6.service.CategoryService;
import com.example.asm6.service.CourseService;
import com.example.asm6.service.ReviewService;
import com.example.asm6.util.MarkdownParser;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MarkdownParser markdownParser;

    // TRANG CHỦ (PUBLIC)
    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        // Mỗi trang hiển thị tối đa 10 khóa học theo yêu cầu (If a page contains more than 10 Courses...)
        Page<Course> coursePage = courseService.getPublishedCourses(page, 10);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        
        // Đám mây danh mục
        model.addAttribute("categories", categoryService.getActiveCategories());
        
        // Danh sách review mới nhất (ví dụ hiển thị 5 cái)
        model.addAttribute("recentReviews", reviewService.getRecentApprovedReviews(5));
        
        model.addAttribute("activePage", "home");
        
        return "home";
    }

    // CHI TIẾT KHÓA HỌC (PUBLIC)
    @GetMapping("/courses/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseService.getCourseById(id);
        
        // Render Markdown ra HTML
        String contentHtml = markdownParser.parse(course.getContent());
        
        model.addAttribute("course", course);
        model.addAttribute("courseContentHtml", contentHtml);
        
        // Danh sách các review đã được duyệt của khóa học này
        List<Review> approvedReviews = reviewService.getApprovedReviewsByCourse(id);
        model.addAttribute("reviews", approvedReviews);
        
        // Đối tượng Review rỗng cho form
        if (!model.containsAttribute("review")) {
            model.addAttribute("review", new Review());
        }
        
        model.addAttribute("activePage", "home");
        
        return "course-details";
    }

    // GỬI REVIEW CHO KHÓA HỌC (PUBLIC)
    @PostMapping("/courses/{id}/reviews")
    public String submitReview(@PathVariable("id") Long courseId,
                               @Valid @ModelAttribute("review") Review review,
                               BindingResult result,
                               RedirectAttributes redirectAttributes,
                               Model model) {
        if (result.hasErrors()) {
            // Nếu có lỗi validation, quay lại trang chi tiết kèm thông báo lỗi
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.review", result);
            redirectAttributes.addFlashAttribute("review", review);
            redirectAttributes.addFlashAttribute("errorMessage", "Vui lòng nhập đầy đủ thông tin đánh giá.");
            return "redirect:/courses/" + courseId;
        }

        reviewService.createReview(courseId, review);
        redirectAttributes.addFlashAttribute("successMessage", "Cảm ơn bạn! Đánh giá của bạn đã được gửi và đang chờ giảng viên phê duyệt.");
        return "redirect:/courses/" + courseId;
    }

    // DANH SÁCH KHÓA HỌC THEO CATEGORY (PUBLIC)
    @GetMapping("/categories/{name}")
    public String categoryCourses(@PathVariable("name") String categoryName,
                                  @RequestParam(defaultValue = "0") int page,
                                  Model model) {
        Page<Course> coursePage = courseService.getPublishedCoursesByCategory(categoryName, page, 10);
        
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("selectedCategory", categoryName);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        
        // Đám mây danh mục
        model.addAttribute("categories", categoryService.getActiveCategories());
        
        model.addAttribute("activePage", "home");
        
        return "category-courses";
    }

    // TRANG REVIEWS GẦN ĐÂY (PUBLIC)
    @GetMapping("/reviews/recent")
    public String recentReviews(Model model) {
        // Lấy 20 reviews gần đây đã duyệt
        model.addAttribute("reviews", reviewService.getRecentApprovedReviews(20));
        model.addAttribute("activePage", "reviews");
        return "recent-reviews";
    }

    // TRANG ĐĂNG NHẬP
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
