package com.example.lms.controller;

import com.example.lms.entity.Course;
import com.example.lms.service.CourseService;
import com.example.lms.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.example.lms.entity.CourseId;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final AccountRepository accountRepository;

    @GetMapping
    public String listCourses(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<Course> coursePage = courseService.getAllCourses(PageRequest.of(page, size));
        model.addAttribute("courses", coursePage.getContent());
        model.addAttribute("page", coursePage);
        model.addAttribute("teachers", accountRepository.findByRoles_Name("TEACHER"));
        return "course_list";
    }

    @GetMapping("/template")
    public org.springframework.http.ResponseEntity<byte[]> downloadTemplate() {
        String content = "Mã khóa học;Tên khóa học;Danh mục;Giảng viên;Ngày khai giảng (YYYY-MM-DD)\n";
        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return org.springframework.http.ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"course_template.csv\"")
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @GetMapping("/export")
    public org.springframework.http.ResponseEntity<byte[]> exportCourses() {
        StringBuilder content = new StringBuilder("Mã khóa học;Tên khóa học;Danh mục;Giảng viên;Ngày khai giảng\n");
        List<Course> courses = courseService.getAllCourses();
        for (Course c : courses) {
            content.append(c.getId().getCourseCode() != null ? c.getId().getCourseCode() : "").append(";")
                   .append(c.getCourseName() != null ? c.getCourseName() : "").append(";")
                   .append(c.getCategory() != null ? c.getCategory() : "").append(";")
                   .append(c.getInstructor() != null ? c.getInstructor() : "").append(";")
                   .append(c.getId().getStartDate() != null ? c.getId().getStartDate().toString() : "").append("\n");
        }
        
        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.toString().getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return org.springframework.http.ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"courses_export.csv\"")
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @PostMapping("/import")
    public String importCourses(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean firstLine = true;
                List<Course> coursesToSave = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    if (firstLine) { firstLine = false; continue; } // skip header
                    String[] data = line.split(";", -1);
                    if (data.length >= 5) {
                        Course course = new Course();
                        CourseId id = new CourseId();
                        id.setCourseCode(data[0].trim());
                        id.setStartDate(LocalDate.parse(data[4].trim(), DateTimeFormatter.ISO_LOCAL_DATE));
                        course.setId(id);
                        course.setCourseName(data[1].trim());
                        course.setCategory(data[2].trim());
                        course.setInstructor(data[3].trim());
                        coursesToSave.add(course);
                    }
                }
                courseService.saveAllCourses(coursesToSave);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/courses";
    }

    @GetMapping("/new")
    public String newCourseForm(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("teachers", accountRepository.findByRoles_Name("TEACHER"));
        return "course_form";
    }

    @PostMapping("/new")
    public String saveCourse(@ModelAttribute Course course) {
        courseService.saveCourse(course);
        return "redirect:/courses";
    }

    @PostMapping("/edit/{courseCode}")
    public String saveEditCourse(@PathVariable String courseCode, 
                                 @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, 
                                 @ModelAttribute Course course) {
        course.setId(new CourseId(courseCode, startDate));
        courseService.saveCourse(course);
        return "redirect:/courses";
    }
}
