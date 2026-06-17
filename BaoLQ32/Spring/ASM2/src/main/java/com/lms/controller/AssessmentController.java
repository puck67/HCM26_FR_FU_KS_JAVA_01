package com.lms.controller;

import com.lms.model.AssessmentMaterial;
import com.lms.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class AssessmentController {

    private final StorageService storageService;
    private final List<AssessmentMaterial> assessments = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/")
    public String index() {
        return "redirect:/assessments";
    }

    @GetMapping("/assessments")
    public String listAssessments(Model model) {
        model.addAttribute("assessments", assessments);
        return "assessments";
    }

    @PostMapping("/assessments/upload")
    public String uploadAssessment(@RequestParam("file") MultipartFile file,
                                   @RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   RedirectAttributes redirectAttributes) {
        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload: Title is required.");
            return "redirect:/assessments";
        }
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload: File is empty.");
            return "redirect:/assessments";
        }

        try {
            String filename = file.getOriginalFilename();
            if (filename == null || filename.isEmpty()) {
                filename = "file_" + UUID.randomUUID().toString();
            }

            storageService.store(file, filename);

            AssessmentMaterial material = new AssessmentMaterial(
                    idCounter.getAndIncrement(),
                    title,
                    description,
                    filename
            );
            assessments.add(material);

            redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to store file: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    @GetMapping("/assessments/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
