package com.example.asm2.controller;

import com.example.asm2.model.AssessmentMaterial;
import com.example.asm2.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storageService;
    private final List<AssessmentMaterial> materials = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadAssessment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {
            if (title == null || title.trim().isEmpty()) {
                throw new IllegalArgumentException("Title cannot be empty.");
            }
            if (file.isEmpty()) {
                throw new IllegalArgumentException("Please select a file to upload.");
            }

            // Store file using storage service
            String fileName = storageService.store(file);

            // Create assessment material object
            AssessmentMaterial material = AssessmentMaterial.builder()
                    .id(idCounter.getAndIncrement())
                    .title(title.trim())
                    .description(description != null ? description.trim() : "")
                    .fileName(fileName)
                    .build();

            // Save to in-memory list
            materials.add(material);

            redirectAttributes.addFlashAttribute("successMessage", "Assessment material uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload failed: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
