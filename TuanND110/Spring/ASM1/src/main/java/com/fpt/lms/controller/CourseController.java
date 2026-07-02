package com.fpt.lms.controller;

import com.fpt.lms.model.Course;
import com.fpt.lms.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequestMapping("/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @GetMapping
    public String listCourses(Model model) {
        List<Course> courses = courseService.findAll();
        log.info("Fetching all courses. Found {} courses in database.", courses.size());
        model.addAttribute("courses", courses);
        return "course_list";
    }

    @GetMapping("/new")
    public String showCreateCourseForm(Model model) {
        model.addAttribute("course", new Course());
        return "create_course";
    }

    @PostMapping("/create")
    public String createCourse(@Valid @ModelAttribute Course course, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        log.info("Attempting to create course: {}", course);
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors: {}", bindingResult.getAllErrors());
            return "create_course";
        }

        Course saved = courseService.save(course);
        log.info("Successfully saved course to database: {}", saved);
        redirectAttributes.addFlashAttribute("successMessage", "Course '" + course.getTitle() + "' created and saved to database successfully!");
        return "redirect:/courses";
    }

    @GetMapping("/success")
    public String showCreateSuccessPage() {
        return "create_success";
    }

    @GetMapping("/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        log.info("Downloading CSV template file.");
        String content = "Title;Instructor Name;Instructor Email;Description;Duration (hours)\n" +
                "Spring Framework;Nguyen Van A;a.nguyen@example.com;Comprehensive guide to Spring;40\n";
        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"course_template.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @GetMapping("/export")
    public ResponseEntity<byte[]> exportCourses() {
        log.info("Exporting all courses from database to CSV.");
        StringBuilder content = new StringBuilder("Title;Instructor Name;Instructor Email;Description;Duration (hours)\n");
        List<Course> courses = courseService.findAll();
        for (Course c : courses) {
            content.append(c.getTitle() != null ? c.getTitle() : "").append(";")
                   .append(c.getInstructorName() != null ? c.getInstructorName() : "").append(";")
                   .append(c.getInstructorEmail() != null ? c.getInstructorEmail() : "").append(";")
                   .append(c.getDescription() != null ? c.getDescription() : "").append(";")
                   .append(c.getDurationHours() != null ? c.getDurationHours().toString() : "").append("\n");
        }

        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.toString().getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"courses_export.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @PostMapping("/import")
    public String importCourses(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        log.info("CSV Import requested. File: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());
        if (file.isEmpty()) {
            log.warn("Import failed: Uploaded file is empty.");
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to import: Selected file is empty.");
            return "redirect:/courses";
        }

        int successCount = 0;
        int failedCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    log.info("Skipping header row: {}", line);
                    continue;
                } // skip header row
                
                if (line.trim().isEmpty()) {
                    continue;
                }

                log.info("Processing row: {}", line);
                String[] data = line.split(";", -1);
                if (data.length >= 5) {
                    try {
                        Course course = Course.builder()
                                .title(data[0].trim())
                                .instructorName(data[1].trim())
                                .instructorEmail(data[2].trim())
                                .description(data[3].trim())
                                .durationHours(Integer.parseInt(data[4].trim()))
                                .build();
                        Course saved = courseService.save(course);
                        log.info("Saved imported course to database: {}", saved);
                        successCount++;
                    } catch (Exception ex) {
                        log.error("Failed to parse row: '{}' due to error: {}", line, ex.getMessage());
                        failedCount++;
                    }
                } else {
                    log.warn("Skipping row: '{}' - expected at least 5 columns, got {}", line, data.length);
                    failedCount++;
                }
            }

            if (failedCount > 0) {
                log.warn("Import finished with errors. Success: {}, Failed: {}", successCount, failedCount);
                redirectAttributes.addFlashAttribute("errorMessage", "Imported " + successCount + " courses. Failed to import " + failedCount + " rows due to invalid data format.");
            } else {
                log.info("Import completed successfully. Imported {} courses.", successCount);
                redirectAttributes.addFlashAttribute("successMessage", "All " + successCount + " courses imported and saved to database successfully!");
            }
        } catch (Exception e) {
            log.error("CSV Import failed due to general exception: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error parsing CSV file: " + e.getMessage());
        }

        return "redirect:/courses";
    }
}
