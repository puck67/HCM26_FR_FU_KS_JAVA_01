package fa.training.jsfwla102.controller;

import fa.training.jsfwla102.entity.Course;
import fa.training.jsfwla102.entity.Instructor;
import fa.training.jsfwla102.entity.Review;
import fa.training.jsfwla102.repository.CategoryRepository;
import fa.training.jsfwla102.repository.CourseRepository;
import fa.training.jsfwla102.repository.InstructorRepository;
import fa.training.jsfwla102.repository.ReviewRepository;
import fa.training.jsfwla102.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorController {

    private final CourseRepository courseRepository;
    private final ReviewRepository reviewRepository;
    private final CategoryRepository categoryRepository;
    private final InstructorRepository instructorRepository;
    private final CourseService courseService;

    public InstructorController(CourseRepository courseRepository, ReviewRepository reviewRepository,
                                CategoryRepository categoryRepository, InstructorRepository instructorRepository,
                                CourseService courseService) {
        this.courseRepository = courseRepository;
        this.reviewRepository = reviewRepository;
        this.categoryRepository = categoryRepository;
        this.instructorRepository = instructorRepository;
        this.courseService = courseService;
    }

    @GetMapping("/login")
    public String login() {
        return "instructor/login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        long totalCourses = courseRepository.count();
        long publishedCourses = courseRepository.countByStatus(2);
        long pendingReviews = reviewRepository.countByStatus(1);
        long totalCategories = categoryRepository.count();

        model.addAttribute("totalCourses", totalCourses);
        model.addAttribute("publishedCourses", publishedCourses);
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("totalCategories", totalCategories);

        return "instructor/dashboard";
    }

    @GetMapping("/courses")
    public String manageCourses(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Course> coursePage = courseRepository.findAll(PageRequest.of(page, 10));
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", coursePage.getTotalPages());
        return "instructor/manage-courses";
    }

    @GetMapping("/courses/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "instructor/course-form";
    }

    @GetMapping("/courses/edit/{id}")
    public String editCourseForm(@PathVariable Long id, Model model) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));
        model.addAttribute("course", course);
        return "instructor/course-form";
    }

    @PostMapping("/courses/save")
    public String saveCourse(@ModelAttribute Course course, Authentication authentication) {
        if (authentication != null && course.getInstructor() == null) {
            String username = authentication.getName();
            Instructor instructor = instructorRepository.findByUsername(username).orElse(null);
            course.setInstructor(instructor);
        }
        
        if (course.getStatus() == null) {
            course.setStatus(1);
        }

        courseService.saveCourse(course);
        return "redirect:/instructor/courses";
    }

    @GetMapping("/reviews")
    public String manageReviews(Model model) {
        List<Review> pendingReviews = reviewRepository.findByStatusOrderByIdDesc(1);
        List<Review> approvedReviews = reviewRepository.findByStatusOrderByIdDesc(2);
        
        model.addAttribute("pendingReviews", pendingReviews);
        model.addAttribute("approvedReviews", approvedReviews);
        return "instructor/reviews";
    }
}
