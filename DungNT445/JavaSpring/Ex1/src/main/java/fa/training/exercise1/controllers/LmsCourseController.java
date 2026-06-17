package fa.training.exercise1.controllers;

import fa.training.exercise1.controllers.base.GenericController;
import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsCourseId;
import fa.training.exercise1.services.LmsCourseService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/courses")
public class LmsCourseController extends GenericController<LmsCourse, LmsCourseId, LmsCourseService> {

    public LmsCourseController(LmsCourseService courseService) {
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
    protected LmsCourse createEmptyEntity() {
        return new LmsCourse();
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
    public String saveCourse(@Valid @ModelAttribute("course") LmsCourse course,
            BindingResult bindingResult,
            Model model) {
        return doSave(course, bindingResult, model);
    }

    @Override
    protected void preSave(LmsCourse course, BindingResult bindingResult) {
        LmsCourseId courseId = new LmsCourseId(course.getCourse_code(), course.getStart_date());
        if (service.existsById(courseId)) {
            bindingResult.rejectValue("course_code", "error.course", "Mã khóa học và ngày khai giảng đã tồn tại!");
        }
    }

    @Override
    protected void postSave(LmsCourse course, Model model) {
        model.addAttribute("successMessage", "Add a new LmsCourse successfully!");
        model.addAttribute("showLessonDetailLink", true);
    }
}

