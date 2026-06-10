package com.example.EX1.controller;

import com.example.EX1.controller.base.GenericController;
import com.example.EX1.model.Course;
import com.example.EX1.model.CourseId;
import com.example.EX1.model.Lesson;
import com.example.EX1.service.CourseService;
import com.example.EX1.service.LessonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * LessonController - kế thừa GenericController<Lesson, Long>.
 * Kế thừa CRUD logic từ GenericController thông qua LessonService.
 *
 * Vì Lesson nằm trong ngữ cảnh của một Course cụ thể,
 * URL có dạng: /courses/{courseCode}/{startDate}/lessons/...
 * → Các endpoint override lại để xử lý path variable đặc thù.
 */
@Controller
@RequestMapping("/courses/{courseCode}/{startDate}/lessons")
public class LessonController extends GenericController<Lesson, Long> {

    private final CourseService courseService;

    public LessonController(LessonService lessonService, CourseService courseService) {
        // viewFolder = "lesson-detail" (map tới template hiện có), modelName = "lesson"
        super(lessonService, "lesson-detail", "lesson");
        this.courseService = courseService;
    }

    @Override
    protected Class<Lesson> getEntityClass() {
        return Lesson.class;
    }

    // ─── Tiện ích nội bộ ──────────────────────────────────────────────────────

    /**
     * Lấy LessonService đã được inject (ép kiểu từ GenericService).
     */
    private LessonService getLessonService() {
        return (LessonService) service;
    }

    /**
     * Build CourseId từ path variables.
     */
    private CourseId buildCourseId(String courseCode, String startDateStr) {
        return new CourseId(courseCode, LocalDate.parse(startDateStr));
    }

    // ─── Endpoints đặc thù của Lesson ────────────────────────────────────────

    /**
     * GET /courses/{courseCode}/{startDate}/lessons
     * Hiển thị danh sách lesson của course + form thêm/sửa lesson.
     * Tương đương handleList() + handleEdit() của GenericController.
     */
    @GetMapping
    public String showLessonDetail(@PathVariable("courseCode") String courseCode,
                                   @PathVariable("startDate") String startDateStr,
                                   @RequestParam(value = "editId", required = false) Long editId,
                                   Model model) {

        CourseId courseId = buildCourseId(courseCode, startDateStr);

        // Tìm Course qua CourseService (GenericService)
        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isEmpty()) {
            return "redirect:/";
        }

        Course course = courseOpt.get();

        // Lấy danh sách Lesson qua LessonService (hàm đặc thù)
        List<Lesson> lessons = getLessonService().findByCourseId(courseId);

        model.addAttribute("course", course);
        model.addAttribute("courseCode", courseCode);
        model.addAttribute("startDate", startDateStr);
        model.addAttribute("lessons", lessons);

        // Nếu đang edit: load lesson vào form; nếu không: tạo lesson rỗng
        if (editId != null) {
            // Gọi service.findById() thông qua GenericService
            service.findById(editId)
                   .ifPresent(lesson -> model.addAttribute("editLesson", lesson));
        }
        if (!model.containsAttribute("editLesson")) {
            model.addAttribute("editLesson", new Lesson());
        }

        return "lesson-detail";
    }

    /**
     * POST /courses/{courseCode}/{startDate}/lessons/save
     * Lưu lesson (thêm mới hoặc cập nhật).
     * Tương đương handleSave() nhưng với logic lồng Course.
     */
    @PostMapping("/save")
    public String saveLesson(@PathVariable("courseCode") String courseCode,
                             @PathVariable("startDate") String startDateStr,
                             @RequestParam(value = "lessonId", required = false) Long lessonId,
                             @RequestParam("lessonName") String lessonName,
                             @RequestParam("duration") Integer duration,
                             @RequestParam("contentType") String contentType,
                             @RequestParam("status") String status) {

        CourseId courseId = buildCourseId(courseCode, startDateStr);

        // Tìm Course qua CourseService (GenericService)
        Optional<Course> courseOpt = courseService.findById(courseId);
        if (courseOpt.isEmpty()) {
            return "redirect:/";
        }

        Course course = courseOpt.get();
        Lesson lesson;

        if (lessonId != null) {
            // Chỉnh sửa lesson cũ: tìm qua GenericService.findById()
            lesson = service.findById(lessonId).orElse(new Lesson());
        } else {
            // Tạo lesson mới
            lesson = new Lesson();
        }

        lesson.setCourse(course);
        lesson.setLessonName(lessonName);
        lesson.setDuration(duration);
        lesson.setContentType(contentType);
        lesson.setStatus(status);

        // Lưu qua GenericService.save()
        service.save(lesson);

        return "redirect:/courses/" + courseCode + "/" + startDateStr + "/lessons";
    }

    /**
     * GET /courses/{courseCode}/{startDate}/lessons/{id}/delete
     * Xóa lesson theo ID.
     * Tương đương handleDelete() nhưng redirect về URL lồng nhau.
     */
    @GetMapping("/{id}/delete")
    public String deleteLesson(@PathVariable("courseCode") String courseCode,
                               @PathVariable("startDate") String startDateStr,
                               @PathVariable("id") Long id) {

        // Gọi GenericService.deleteById()
        service.deleteById(id);

        return "redirect:/courses/" + courseCode + "/" + startDateStr + "/lessons";
    }
}
