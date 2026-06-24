package com.example.lms.controller;

import com.example.lms.entity.Student;
import com.example.lms.repository.StudentRepository;
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

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public String listStudents(@RequestParam(defaultValue = "0") int page,
                               @RequestParam(defaultValue = "10") int size,
                               Model model) {
        Page<Student> studentPage = studentRepository.findAll(PageRequest.of(page, size));
        model.addAttribute("students", studentPage.getContent());
        model.addAttribute("page", studentPage);
        return "student_list";
    }

    @PostMapping("/new")
    public String addStudent(@ModelAttribute Student student) {
        studentRepository.save(student);
        return "redirect:/students";
    }

    @PostMapping("/edit/{id}")
    public String saveEditStudent(@PathVariable String id, @ModelAttribute Student student) {
        student.setStudentCode(id); // Ensure ID doesn't change
        studentRepository.save(student);
        return "redirect:/students";
    }

    @GetMapping("/template")
    public org.springframework.http.ResponseEntity<byte[]> downloadTemplate() {
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
    public org.springframework.http.ResponseEntity<byte[]> exportStudents() {
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
    public String importStudents(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
                String line;
                boolean firstLine = true;
                List<Student> studentsToSave = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    if (firstLine) { firstLine = false; continue; } // skip header
                    String[] data = line.split(",", -1);
                    if (data.length >= 3) {
                        Student student = new Student();
                        student.setStudentCode(data[0].trim());
                        student.setFullName(data[1].trim());
                        student.setEmail(data[2].trim());
                        studentsToSave.add(student);
                    }
                }
                studentRepository.saveAll(studentsToSave);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "redirect:/students";
    }
}
