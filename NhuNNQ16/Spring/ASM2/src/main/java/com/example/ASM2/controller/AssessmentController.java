package com.example.ASM2.controller;

import com.example.ASM2.model.AssessmentMaterial;
import com.example.ASM2.service.StorageService;
import com.example.ASM2.service.StorageException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Task 3 – Handles all HTTP requests for the assessment upload/download feature.
 *
 * Endpoints:
 *   GET  /assessments                        → display upload page + material list
 *   POST /assessments/upload                 → handle file upload form submission
 *   GET  /assessments/download/{filename}    → serve a file for download
 */
@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storageService;
    
    // In-memory list to store AssessmentMaterial metadata (Task 1)
    private final List<AssessmentMaterial> materials = Collections.synchronizedList(new ArrayList<>());
    
    // Thread-safe counter to generate unique IDs for uploaded files (Task 1)
    private final AtomicLong idGenerator = new AtomicLong(1);

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }


    @GetMapping
    public String listAssessments(Model model) {
        List<AssessmentMaterial> reversedList = new ArrayList<>(materials);
        Collections.reverse(reversedList);
        
        model.addAttribute("materials", reversedList);
        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadAssessment(@RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        // Validation checks
        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Title cannot be empty!");
            return "redirect:/assessments";
        }
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload!");
            return "redirect:/assessments";
        }

        try {
            // Task 6: Attempt storing the file via storageService.
            // If storage fails, StorageException will be caught in the catch block.
            String storedFilename = storageService.store(file);

            // Create assessment metadata and save to list
            AssessmentMaterial material = new AssessmentMaterial(
                    idGenerator.getAndIncrement(),
                    title.trim(),
                    description == null ? "" : description.trim(),
                    storedFilename
            );
            materials.add(material);

            redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully: " + storedFilename);
        } catch (StorageException e) {
            // Task 6: Error handling for file storage failures
            redirectAttributes.addFlashAttribute("errorMessage", "Upload failed: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource fileResource = storageService.loadAsResource(filename);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileResource.getFilename() + "\"")
                    .body(fileResource);
        } catch (StorageException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
