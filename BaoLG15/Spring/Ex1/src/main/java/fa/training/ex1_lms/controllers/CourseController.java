package fa.training.ex1_lms.controllers;

import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.CourseId;
import fa.training.ex1_lms.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        model.addAttribute("activePage", "courses");
        return "course_list";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("isNew", true);
        model.addAttribute("activePage", "new-course");
        return "course_form";
    }

    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult,
            Model model) {
        model.addAttribute("activePage", "new-course");

        if (bindingResult.hasErrors()) {
            model.addAttribute("isNew", true);
            return "course_form";
        }

        // Check duplicate composite key
        CourseId courseId = new CourseId(course.getCourse_code(), course.getStart_date());
        if (courseService.existsById(courseId)) {
            bindingResult.rejectValue("course_code", "error.course", "Mã khóa học và ngày khai giảng đã tồn tại!");
            model.addAttribute("isNew", true);
            return "course_form";
        }

        courseService.save(course);
        model.addAttribute("successMessage", "Add a new Course successfully!");
        model.addAttribute("showLessonDetailLink", true);
        model.addAttribute("isNew", false);
        return "course_form";
    }
}
