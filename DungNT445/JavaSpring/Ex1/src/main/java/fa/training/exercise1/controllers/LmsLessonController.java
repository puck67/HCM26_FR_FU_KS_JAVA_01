package fa.training.exercise1.controllers;

import fa.training.exercise1.controllers.base.GenericController;
import fa.training.exercise1.entities.LmsCourse;
import fa.training.exercise1.entities.LmsCourseId;
import fa.training.exercise1.entities.LmsLesson;
import fa.training.exercise1.services.LmsCourseService;
import fa.training.exercise1.services.LmsLessonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class LmsLessonController extends GenericController<LmsLesson, Long, LmsLessonService> {

    private final LmsCourseService courseService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LmsLessonController(LmsCourseService courseService, LmsLessonService lessonService) {
        super(lessonService);
        this.courseService = courseService;
    }

    @Override
    protected String getListView() {
        return "lesson_detail";
    }

    @Override
    protected String getFormView() {
        return "lesson_detail";
    }

    @Override
    protected String getListName() {
        return "lessons";
    }

    @Override
    protected String getEntityName() {
        return "lesson";
    }

    @Override
    protected String getActivePageList() {
        return "courses";
    }

    @Override
    protected String getActivePageForm() {
        return "courses";
    }

    @Override
    protected LmsLesson createEmptyEntity() {
        return new LmsLesson();
    }

    @GetMapping("/{courseCode}/{startDate}/lessons")
    public String showLessonDetail(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            Model model) {

        LmsCourseId courseId = new LmsCourseId(courseCode, startDate);
        LmsCourse course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        List<LmsLesson> lessons = service.getLessonsByCourse(course);

        LmsLesson lesson = createEmptyEntity();
        lesson.setLmsCourse(course);

        model.addAttribute("course", course);
        model.addAttribute(getListName(), lessons);
        model.addAttribute(getEntityName(), lesson);
        model.addAttribute("activePage", getActivePageList());

        return getListView();
    }

    @PostMapping("/{courseCode}/{startDate}/lessons/save")
    public String saveLesson(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @ModelAttribute("lesson") LmsLesson lesson) {

        LmsCourseId courseId = new LmsCourseId(courseCode, startDate);
        LmsCourse course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        lesson.setLmsCourse(course);
        service.save(lesson);

        return "redirect:/courses/" + courseCode + "/" + dateFormat.format(startDate) + "/lessons";
    }

    @GetMapping("/{courseCode}/{startDate}/lessons/edit/{id}")
    public String editLesson(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("id") Long id,
            Model model) {

        LmsCourseId courseId = new LmsCourseId(courseCode, startDate);
        LmsCourse course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        LmsLesson lesson = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bài học không tồn tại"));

        List<LmsLesson> lessons = service.getLessonsByCourse(course);

        model.addAttribute("course", course);
        model.addAttribute(getListName(), lessons);
        model.addAttribute(getEntityName(), lesson);
        model.addAttribute("activePage", getActivePageList());

        return getListView();
    }

    @GetMapping("/{courseCode}/{startDate}/lessons/delete/{id}")
    public String deleteLesson(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("id") Long id) {

        doDelete(id);

        return "redirect:/courses/" + courseCode + "/" + dateFormat.format(startDate) + "/lessons";
    }
}

