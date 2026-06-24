package com.example.lms.controller;

import com.example.lms.model.AssessmentMaterial;
import com.example.lms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class AssessmentController {

    private final StorageService storageService;
    private final List<AssessmentMaterial> materials = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    @Autowired
    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/assessments")
    public String listMaterials(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    @PostMapping("/assessments/upload")
    public String upload(@RequestParam("title") String title,
                         @RequestParam("description") String description,
                         @RequestParam("file") MultipartFile file,
                         RedirectAttributes redirectAttributes) {
        try {
            String fileName = storageService.store(file);
            materials.add(new AssessmentMaterial(counter.incrementAndGet(), title, description, fileName));
            redirectAttributes.addFlashAttribute("message",
                    new StringBuilder("Uploaded successfully: ").append(fileName).toString());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error",
                    new StringBuilder("Upload failed: ").append(e.getMessage()).toString());
        }
        return "redirect:/assessments";
    }

    @GetMapping("/assessments/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> download(@PathVariable String filename) {
        Resource resource = storageService.load(filename);
        String disposition = new StringBuilder("attachment; filename=\"")
                .append(filename)
                .append("\"")
                .toString();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, disposition)
                .body(resource);
    }
}
