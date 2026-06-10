package fa.training.ex1_lms.controllers;

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
public class LessonController {

    private final CourseService courseService;
    private final LessonService lessonService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public LessonController(CourseService courseService, LessonService lessonService) {
        this.courseService = courseService;
        this.lessonService = lessonService;
    }

    @GetMapping("/{courseCode}/{startDate}/lessons")
    public String showLessonDetail(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            Model model) {

        CourseId courseId = new CourseId(courseCode, startDate);
        Course course = courseService.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Khóa học không tồn tại"));

        List<Lesson> lessons = lessonService.getLessonsByCourse(course);

        Lesson lesson = new Lesson();
        lesson.setCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("lesson", lesson);
        model.addAttribute("activePage", "courses");

        return "lesson_detail";
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
        lessonService.save(lesson);

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

        Lesson lesson = lessonService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Bài học không tồn tại"));

        List<Lesson> lessons = lessonService.getLessonsByCourse(course);

        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("lesson", lesson);
        model.addAttribute("activePage", "courses");

        return "lesson_detail";
    }

    @GetMapping("/{courseCode}/{startDate}/lessons/delete/{id}")
    public String deleteLesson(
            @PathVariable("courseCode") String courseCode,
            @PathVariable("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @PathVariable("id") Long id) {

        lessonService.deleteById(id);

        return "redirect:/courses/" + courseCode + "/" + dateFormat.format(startDate) + "/lessons";
    }
}
