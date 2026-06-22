package fa.training.hungplt1.asm6.controller;

import fa.training.hungplt1.asm6.entity.Course;
import fa.training.hungplt1.asm6.entity.Review;
import fa.training.hungplt1.asm6.repository.CategoryRepository;
import fa.training.hungplt1.asm6.repository.CourseRepository;
import fa.training.hungplt1.asm6.repository.ReviewRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/")
public class PublicController {
    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;

    public PublicController(CourseRepository courseRepository, ReviewRepository reviewRepository, CategoryRepository categoryRepository) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
    }

    @GetMapping
    public String homePage(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = courseRepository.findByStatusOrderByIdDesc(2, PageRequest.of(page, 10));

        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());

        model.addAttribute("categories", categoryRepository.findAllByOrderByFrequencyDesc());
        model.addAttribute("recentReviews", reviewRepository.findTop5ByStatusOrderByIdDesc(2));

        return "public/home";
    }

    @GetMapping("/course/{id}")
    public String courseDetails(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        model.addAttribute("course", course);
        model.addAttribute("newReview", new Review());
        return "public/course-details";
    }

    @PostMapping("/course/{id}/review")
    public String submitReview(@PathVariable Long id, @ModelAttribute Review review) {
        Course course = courseRepository.findById(id)
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
        Page<Course> coursePage = courseRepository.findPublishedCoursesByCategory(name, PageRequest.of(page, 10));

        model.addAttribute("categoryName", name);
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        model.addAttribute("categories", categoryRepository.findAllByOrderByFrequencyDesc());

        return "public/category-list";
    }
}
