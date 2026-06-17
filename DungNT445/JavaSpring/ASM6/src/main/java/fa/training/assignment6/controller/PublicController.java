package fa.training.assignment6.controller;

import fa.training.assignment6.entity.LmsCourse;
import fa.training.assignment6.entity.CourseReview;
import fa.training.assignment6.repository.CourseCategoryRepository;
import fa.training.assignment6.repository.LmsCourseRepository;
import fa.training.assignment6.repository.CourseReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PublicController {
    private final LmsCourseRepository courseRepository;
    private final CourseReviewRepository reviewRepository;
    private final CourseCategoryRepository categoryRepository;

    public PublicController(LmsCourseRepository courseRepository, CourseReviewRepository reviewRepository, CourseCategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<LmsCourse> coursePage = courseRepository.findByStatusOrderByIdDesc(2, PageRequest.of(page, 10));

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());

        model.addAttribute("categories", categoryRepository.findAllByOrderByFrequencyDesc());
        model.addAttribute("recentReviews", reviewRepository.findTop5ByStatusOrderByIdDesc(2));

        return "public/home";
    }

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        LmsCourse course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        model.addAttribute("course", course);
        model.addAttribute("newReview", new CourseReview());
        return "public/course-details";
    }

    @PostMapping("/course/{id}/review")
    public String submitReview(@PathVariable Long id, @ModelAttribute CourseReview review) {
        LmsCourse course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));
        review.setId(null);
        review.setCourse(course);
        review.setStatus(1);
        reviewRepository.save(review);
        return "redirect:/course/" + id + "?success=true";
    }

    @GetMapping("/category/{name}")
    public String coursesByCategory(@PathVariable String name,
                                    @RequestParam(defaultValue = "0") int page, Model model) {
        Page<LmsCourse> coursePage = courseRepository.findPublishedCoursesByCategory(name, PageRequest.of(page, 10));

        model.addAttribute("categoryName", name);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAllByOrderByFrequencyDesc());

        return "public/category-list";
    }
}
