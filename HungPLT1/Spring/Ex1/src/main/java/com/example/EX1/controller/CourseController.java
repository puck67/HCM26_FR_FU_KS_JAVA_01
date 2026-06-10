package com.example.EX1.controller;

import com.example.EX1.controller.base.GenericController;
import com.example.EX1.model.Course;
import com.example.EX1.model.CourseId;
import com.example.EX1.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

/**
 * CourseController - kế thừa GenericController<Course, CourseId>.
 * Kế thừa toàn bộ CRUD logic từ lớp cha thông qua CourseService.
 * Controller này chỉ cần viết thêm logic đặc thù của Course:
 *   - Xử lý tham số form (@RequestParam) thay vì @ModelAttribute
 *     vì Course dùng @EmbeddedId (CourseId) không bind tự động được.
 *   - Trả về flash message sau khi lưu thành công.
 */
@Controller
@RequestMapping("/courses")
public class CourseController extends GenericController<Course, CourseId> {

    public CourseController(CourseService courseService) {
        // viewFolder = "courses", modelName = "course"
        super(courseService, "courses", "course");
    }

    @Override
    protected Class<Course> getEntityClass() {
        return Course.class;
    }

    // ─── Endpoints đặc thù của Course ─────────────────────────────────────────

    /**
     * GET /courses → Hiển thị danh sách khóa học.
     */
    @GetMapping
    public String listCourses(Model model) {
        model.addAttribute("courses", service.findAll());
        return "course-list";
    }

    /**
     * GET /courses/new → Hiển thị form thêm mới Course.
     * Gọi handleCreate() từ lớp cha để tạo Course rỗng và đưa vào model.
     */
    @GetMapping("/new")
    public String showAddCourseForm(Model model) {
        // Template của dự án tên là "course-form" (không theo quy tắc viewFolder/form)
        model.addAttribute("course", new Course());
        return "course-form";
    }

    /**
     * POST /courses/save → Lưu Course mới vào database.
     * Dùng @RequestParam vì CourseId là @EmbeddedId (composite key).
     */
    @PostMapping("/save")
    public String saveCourse(@RequestParam("courseCode") String courseCode,
                             @RequestParam("startDate") String startDateStr,
                             @RequestParam("courseName") String courseName,
                             @RequestParam("category") String category,
                             @RequestParam("instructor") String instructor,
                             RedirectAttributes redirectAttributes) {

        LocalDate startDate = LocalDate.parse(startDateStr);
        CourseId courseId = new CourseId(courseCode, startDate);

        Course course = new Course();
        course.setId(courseId);
        course.setCourseName(courseName);
        course.setCategory(category);
        course.setInstructor(instructor);

        // Gọi service.save() thông qua helper của GenericController
        service.save(course);

        redirectAttributes.addFlashAttribute("successMessage", "Add a new Course successfully");
        redirectAttributes.addFlashAttribute("savedCourseCode", courseCode);
        redirectAttributes.addFlashAttribute("savedStartDate", startDateStr);
        redirectAttributes.addFlashAttribute("savedCourseName", courseName);
        redirectAttributes.addFlashAttribute("savedCategory", category);
        redirectAttributes.addFlashAttribute("savedInstructor", instructor);

        return "redirect:/courses/new";
    }
}
