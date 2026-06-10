package lms.controller;

import entity.Course;
import entity.CourseId;
import lms.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    @GetMapping("/management")
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        model.addAttribute("courses", courses);
        return "courses_management";
    }

    @GetMapping("/add")
    public String showAddCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "course_add";
    }

    @PostMapping("/add")
    public String saveCourse(@ModelAttribute("course") Course course, RedirectAttributes redirectAttributes, Model model) {
        courseService.save(course);
        model.addAttribute("successMessage", "Add a new Course successfully!");
        model.addAttribute("course", course);
        return "course_add";
    }

    @GetMapping("/delete/{courseCode}/{startDate}")
    public String deleteCourse(@PathVariable String courseCode,
                               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               RedirectAttributes redirectAttributes) {
        CourseId courseId = new CourseId(courseCode, startDate);
        courseService.deleteById(courseId);
        redirectAttributes.addFlashAttribute("successMessage", "Course deleted successfully!");
        return "redirect:/courses/management";
    }
}
