package fa.training.lms.controller;



import javax.validation.Valid;
import fa.training.lms.model.Course;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CourseController {

    @GetMapping("/")
    public String home() {
        return "redirect:/courses/new";
    }

    @GetMapping("/courses/new")
    public String showCreateForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/courses/create")
    public String createCourse(@Valid Course course,
                               BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            return "create_course";
        }

        return "redirect:/courses/success";
    }

    @GetMapping("/courses/success")
    public String successPage() {
        return "create_success";
    }
}