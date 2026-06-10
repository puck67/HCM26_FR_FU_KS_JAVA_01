package fa.training.ex1_lms.controllers;

import fa.training.ex1_lms.controllers.base.GenericController;
import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.CourseId;
import fa.training.ex1_lms.services.CourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class CourseController extends GenericController<Course, CourseId, CourseService> {

    public CourseController(CourseService courseService) {
        super(courseService);
    }

    @Override
    protected String getListView() {
        return "course_list";
    }

    @Override
    protected String getFormView() {
        return "course_form";
    }

    @Override
    protected String getListName() {
        return "courses";
    }

    @Override
    protected String getEntityName() {
        return "course";
    }

    @Override
    protected String getActivePageList() {
        return "courses";
    }

    @Override
    protected String getActivePageForm() {
        return "new-course";
    }

    @Override
    protected Course createEmptyEntity() {
        return new Course();
    }

    @GetMapping
    public String listCourses(Model model) {
        return doList(model);
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        return doShowCreateForm(model);
    }

    @PostMapping("/save")
    public String saveCourse(@Valid @ModelAttribute("course") Course course,
            BindingResult bindingResult,
            Model model) {
        return doSave(course, bindingResult, model);
    }

    @Override
    protected void preSave(Course course, BindingResult bindingResult) {
        CourseId courseId = new CourseId(course.getCourse_code(), course.getStart_date());
        if (service.existsById(courseId)) {
            bindingResult.rejectValue("course_code", "error.course", "Mã khóa học và ngày khai giảng đã tồn tại!");
        }
    }

    @Override
    protected void postSave(Course course, Model model) {
        model.addAttribute("successMessage", "Add a new Course successfully!");
        model.addAttribute("showLessonDetailLink", true);
    }
}

