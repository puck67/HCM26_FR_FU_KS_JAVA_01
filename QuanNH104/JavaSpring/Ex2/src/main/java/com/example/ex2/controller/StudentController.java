package com.example.ex2.controller;

import com.example.ex2.entity.Course;
import com.example.ex2.entity.Student;
import com.example.ex2.service.StudentService;
import com.example.ex2.validation.Validators;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    private String renderLayout(Model model, String activePage, String contentFragment) {
        model.addAttribute("activePage", activePage);
        model.addAttribute("content", contentFragment);
        return "layout";
    }

    // 1. Danh sách học viên + Tìm kiếm / Lọc
    @GetMapping
    public String listStudents(@RequestParam(value = "searchName", required = false) String searchName,
                               @RequestParam(value = "searchAge", required = false) Integer searchAge,
                               Model model) {
        List<Student> students;
        if (searchName != null && !searchName.trim().isEmpty()) {
            students = studentService.findStudentsByName(searchName);
            model.addAttribute("searchName", searchName);
        } else if (searchAge != null) {
            students = studentService.findStudentsOlderThan(searchAge);
            model.addAttribute("searchAge", searchAge);
        } else {
            students = studentService.getAllStudents();
        }

        model.addAttribute("students", students);
        return renderLayout(model, "students-management", "students/list :: body");
    }

    // 2. Màn hình thêm mới học viên
    @GetMapping("/create")
    public String createForm(Model model) {
        model.addAttribute("student", new Student());
        return renderLayout(model, "students-management", "students/form :: body");
    }

    // 3. Lưu học viên mới
    @PostMapping("/save")
    public String saveStudent(@ModelAttribute("student") Student student, BindingResult bindingResult, Model model) {
        Validators.validateStudent(student, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", "Thông tin học viên không hợp lệ!");
            return renderLayout(model, "students-management", "students/form :: body");
        }

        studentService.saveStudent(student);
        return "redirect:/students";
    }

    // 4. Màn hình cập nhật học viên
    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable("id") int id, Model model) {
        try {
            Student student = studentService.getStudent(id);
            model.addAttribute("student", student);
            model.addAttribute("isEdit", true);
            return renderLayout(model, "students-management", "students/form :: body");
        } catch (Exception e) {
            return "redirect:/students";
        }
    }

    // 5. Cập nhật thông tin học viên
    @PostMapping("/update/{id}")
    public String updateStudent(@PathVariable("id") int id, @ModelAttribute("student") Student student, BindingResult bindingResult, Model model) {
        Validators.validateStudent(student, bindingResult);

        if (bindingResult.hasErrors()) {
            model.addAttribute("isEdit", true);
            model.addAttribute("errorMessage", "Thông tin cập nhật không hợp lệ!");
            return renderLayout(model, "students-management", "students/form :: body");
        }

        try {
            Student existing = studentService.getStudent(id);
            existing.setName(student.getName());
            existing.setAge(student.getAge());
            studentService.updateStudent(existing);
            return "redirect:/students";
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Lỗi: " + e.getMessage());
            return renderLayout(model, "students-management", "students/form :: body");
        }
    }

    // 6. Xóa học viên
    @GetMapping("/delete/{id}")
    public String deleteStudent(@PathVariable("id") int id) {
        try {
            studentService.deleteStudent(id);
        } catch (Exception ignored) {
        }
        return "redirect:/students";
    }

    // 7. Chi tiết học viên (hiển thị thông tin & các khóa học tham gia)
    @GetMapping("/details/{id}")
    public String studentDetails(@PathVariable("id") int id, Model model) {
        try {
            Student student = studentService.getStudent(id);
            List<Course> enrolledCourses = studentService.getCoursesOfStudent(id);
            model.addAttribute("student", student);
            model.addAttribute("enrolledCourses", enrolledCourses);
            return renderLayout(model, "students-management", "students/details :: body");
        } catch (Exception e) {
            return "redirect:/students";
        }
    }
}
