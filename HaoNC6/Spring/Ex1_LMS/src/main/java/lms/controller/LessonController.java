package lms.controller;

import entity.Course;
import entity.CourseId;
import entity.Lesson;
import lms.service.CourseService;
import lms.service.LessonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/courses/{courseCode}/{startDate}/lessons")
public class LessonController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private LessonService lessonService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }

    private Course getCourse(String courseCode, Date startDate) {
        CourseId courseId = new CourseId(courseCode, startDate);
        return courseService.findById(courseId).orElse(null);
    }

    @GetMapping
    public String showLessonDetail(@PathVariable String courseCode, 
                                   @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, 
                                   Model model) {
        Course course = getCourse(courseCode, startDate);
        if (course == null) {
            return "redirect:/"; // Or a 404 page
        }

        List<Lesson> lessons = lessonService.getLessonsByCourse(course);
        
        model.addAttribute("course", course);
        model.addAttribute("lessons", lessons);
        model.addAttribute("newLesson", new Lesson());
        
        return "lesson_detail";
    }

    @PostMapping("/save")
    public String saveLesson(@PathVariable String courseCode, 
                             @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                             @ModelAttribute("newLesson") Lesson lesson) {
        Course course = getCourse(courseCode, startDate);
        if (course != null) {
            lesson.setCourse(course);
            lessonService.save(lesson);
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "redirect:/courses/" + courseCode + "/" + sdf.format(startDate) + "/lessons";
    }

    @GetMapping("/delete/{lessonId}")
    public String deleteLesson(@PathVariable String courseCode, 
                               @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
                               @PathVariable Long lessonId) {
        lessonService.deleteById(lessonId);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return "redirect:/courses/" + courseCode + "/" + sdf.format(startDate) + "/lessons";
    }
}
