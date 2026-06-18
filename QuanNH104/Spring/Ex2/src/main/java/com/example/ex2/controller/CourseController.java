package com.example.ex2.controller;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;
import com.example.ex2.service.CourseService;
import com.example.ex2.validation.Validators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // 1. Danh sách khóa học + số lượng học sinh của từng khóa (Aggregation)
    @GetMapping
    public String listCourses(@RequestParam(value = "searchCredit", required = false) Integer searchCredit,
                              Model model) {
        List<Object[]> courseListWithCounts = new ArrayList<>();

        if (searchCredit != null) {
            List<Course> filteredCourses = courseService.findCoursesWithCreditGreaterThan(searchCredit);
            for (Course c : filteredCourses) {
                courseListWithCounts.add(new Object[]{c, (long) c.getStudents().size()});
            }
            model.addAttribute("searchCredit", searchCredit);
        } else {
            courseListWithCounts = courseService.getStudentCountPerCourse();
        }

        model.addAttribute("coursesWithCounts", courseListWithCounts);
        return renderLayout(model, "courses-management", "courses/list :: body");
    }

    // 2. Màn hình thêm mới khóa học
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("course", new Course());
        return renderLayout(model, "courses-management", "courses/form :: body");
    }

    // 3. Lưu khóa học mới
    @PostMapping("/save")
    public String saveCourse(@ModelAttribute("course") Course course, BindingResult bindingResult, Model model) {
        Validators.validateCourse(course, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Thông tin khóa học không hợp lệ!");
            return renderLayout(model, "courses-management", "courses/form :: body");
        }

        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    // 4. Màn hình cập nhật khóa học
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id, Model model) {
        try {
            Course course = courseService.getCourse(id);
            model.addAttribute("course", course);
            model.addAttribute("isEdit", true);
            return renderLayout(model, "courses-management", "courses/form :: body");
        } catch (Exception e) {
            return "redirect:/courses";
        }
    }

    // 5. Cập nhật thông tin khóa học
    @PostMapping("/update/{id}")
    public String updateCourse(@PathVariable("id") int id, @ModelAttribute("course") Course course, BindingResult bindingResult, Model model) {
        Validators.validateCourse(course, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("errorMessage", "Thông tin cập nhật không hợp lệ!");
            return renderLayout(model, "courses-management", "courses/form :: body");
        }

        try {
            Course existing = courseService.getCourse(id);
            existing.setTitle(course.getTitle());
            existing.setCredit(course.getCredit());
            courseService.updateCourse(existing);
            return "redirect:/courses";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return renderLayout(model, "courses-management", "courses/form :: body");
        }
    }

    // 6. Xóa khóa học
    @GetMapping("/delete/{id}")
    public String deleteCourse(@PathVariable("id") int id) {
        try {
            courseService.deleteCourse(id);
        } catch (Exception ignored) {
        }
        return "redirect:/courses";
    }

    // 7. Chi tiết khóa học (thông tin + danh sách học viên đăng ký)
    @GetMapping("/details/{id}")
    public String courseDetails(@PathVariable("id") int id, Model model) {
        try {
            Course course = courseService.getCourse(id);
            List<Student> enrolledStudents = courseService.getStudentsOfCourse(id);
            model.addAttribute("course", course);
            model.addAttribute("enrolledStudents", enrolledStudents);
            return renderLayout(model, "courses-management", "courses/details :: body");
        } catch (Exception e) {
            return "redirect:/courses";
        }
    }
}
