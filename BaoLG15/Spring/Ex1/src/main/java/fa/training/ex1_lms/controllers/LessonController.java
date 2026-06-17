package fa.training.ex1_lms.controllers;

import fa.training.ex1_lms.controllers.base.GenericController;
import fa.training.ex1_lms.entities.Course;
import fa.training.ex1_lms.entities.CourseId;
import fa.training.ex1_lms.entities.Lesson;
import fa.training.ex1_lms.services.CourseService;
import fa.training.ex1_lms.services.LessonService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class LessonController extends GenericController<Lesson, Long, LessonService> {

    private final CourseService courseService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LessonController(CourseService courseService, LessonService lessonService) {
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
    protected Lesson createEmptyEntity() {
        return new Lesson();
    }

    @GetMapping("/{courseCode}/{startDate}/lessons")
    public String showLessonDetail(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            Model model) {

        CourseId courseId = new CourseId(courseCode, startDate);
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        List<Lesson> lessons = service.getLessonsByCourse(course);

        Lesson lesson = createEmptyEntity();
        lesson.setCourse(course);

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
            @ModelAttribute("lesson") Lesson lesson) {

        CourseId courseId = new CourseId(courseCode, startDate);
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        lesson.setCourse(course);
        service.save(lesson);

        return "redirect:/courses/" + courseCode + "/" + dateFormat.format(startDate) + "/lessons";
    }

    @GetMapping("/{courseCode}/{startDate}/lessons/edit/{id}")
    public String editLesson(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("id") Long id,
            Model model) {

        CourseId courseId = new CourseId(courseCode, startDate);
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        Lesson lesson = service.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bài học không tồn tại"));

        List<Lesson> lessons = service.getLessonsByCourse(course);

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

