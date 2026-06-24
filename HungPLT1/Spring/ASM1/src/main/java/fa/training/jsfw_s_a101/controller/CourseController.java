package fa.training.jsfw_s_a101.controller;

import fa.training.jsfw_s_a101.model.Course;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CourseController {

    @GetMapping("/courses/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/courses/create")
    public String createCourse(@Valid @ModelAttribute("course") Course course, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "create_course";
        }
        return "redirect:/courses/success";
    }

    @GetMapping("/courses/success")
    public String showSuccessPage() {
        return "create_success";
    }
}
