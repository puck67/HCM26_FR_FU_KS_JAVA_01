package com.example.lms.controller;

import com.example.lms.entity.Student;
import com.example.lms.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    private static final Logger log = LoggerFactory.getLogger(StudentController.class);

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String listAllStudents(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(defaultValue = "10") int size,
                                  Model model) {
        log.info("Request to list all students: page={}, size={}", page, size);
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("page", studentPage);
        return "student_list";
    }

    @PostMapping("/new")
    public String addNewStudent(@ModelAttribute Student student) {
        log.info("Request to add student: code={}, name={}", student.getStudentCode(), student.getFullName());
        studentRepository.save(student);
        return "redirect:/students";
    }

    @PostMapping("/edit/{id}")
    public String updateExistingStudent(@PathVariable String id, @ModelAttribute Student student) {
        log.info("Request to edit student: id={}", id);
        student.setStudentCode(id); // Ensure ID doesn't change
        studentRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/template")
    public org.springframework.http.ResponseEntity<byte[]> downloadStudentTemplate() {
        log.info("Request to download student CSV template");
        String content = "Mã sinh viên,Họ tên,Email\n";
        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return org.springframework.http.ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"student_template.csv\"")
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @GetMapping("/export")
    public org.springframework.http.ResponseEntity<byte[]> exportStudentsToCsv() {
        log.info("Request to export students to CSV");
        StringBuilder content = new StringBuilder("Mã sinh viên,Họ tên,Email\n");
        List<Student> students = studentRepository.findAll();
        for (Student s : students) {
            content.append(s.getStudentCode() != null ? s.getStudentCode() : "").append(",")
                   .append(s.getFullName() != null ? s.getFullName() : "").append(",")
                   .append(s.getEmail() != null ? s.getEmail() : "").append("\n");
        }
        
        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.toString().getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return org.springframework.http.ResponseEntity.ok()
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"students_export.csv\"")
                .contentType(org.springframework.http.MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @PostMapping("/import")
    public String importStudentsFromCsv(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("Uploaded file is empty");
            return "redirect:/students";
        }
        log.info("Request to import students from file: {}", file.getOriginalFilename());
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            List<Student> studentsToSave = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                if (firstLine) { firstLine = false; continue; } // skip header
                String[] data = line.split(",", -1);
                if (data.length >= 3) {
                    try {
                        Student student = new Student();
                        student.setStudentCode(data[0].trim());
                        student.setFullName(data[1].trim());
                        student.setEmail(data[2].trim());
                        studentsToSave.add(student);
                    } catch (Exception ex) {
                        log.error("Error parsing CSV line: {}. Skipping record.", line, ex);
                    }
                }
            }
            if (!studentsToSave.isEmpty()) {
                studentRepository.saveAll(studentsToSave);
                log.info("Successfully imported {} student(s)", studentsToSave.size());
            }
        } catch (Exception e) {
            log.error("Failed to import students from file", e);
        }
        return "redirect:/students";
    }
}
