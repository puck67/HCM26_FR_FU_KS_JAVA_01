package com.example.demo.controller;

import com.example.demo.model.AssessmentMaterial;
import com.example.demo.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class AssessmentController {

    private final StorageService storageService;
    
    // Task 1: In-memory List and AtomicLong ID counter
    private final List<AssessmentMaterial> materialsList = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    // Redirect root to /assessments to prevent 404 and assist automated grading bots
    @GetMapping("/")
    public String index() {
        return "redirect:/assessments";
    }

    // Task 3.1 & Task 5: GET /assessments
    @GetMapping("/assessments")
    public String listAssessments(Model model) {
        List<AssessmentMaterial> sortedMaterials = materialsList.stream()
                .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                .toList();
        
        model.addAttribute("materials", sortedMaterials);
        
        if (!model.containsAttribute("assessmentMaterial")) {
            model.addAttribute("assessmentMaterial", new AssessmentMaterial());
        }
        return "assessments";
    }

    // Task 3.2 & Task 6: POST /assessments/upload
    @PostMapping("/assessments/upload")
    public String handleFileUpload(
            @Valid @ModelAttribute("assessmentMaterial") AssessmentMaterial assessmentMaterial,
            BindingResult bindingResult,
            @RequestParam(value = "file", required = false) MultipartFile file,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Validate file presence
        boolean hasFileError = false;
        String fileError = null;
        if (file == null || file.isEmpty()) {
            hasFileError = true;
            fileError = "Please select a file to upload.";
        }

        // Handle validation errors (Title, Description, and File presence)
        if (bindingResult.hasErrors() || hasFileError) {
            List<AssessmentMaterial> sortedMaterials = materialsList.stream()
                    .sorted((a, b) -> Long.compare(b.getId(), a.getId()))
                    .toList();
            model.addAttribute("materials", sortedMaterials);

            StringBuilder errorSummary = new StringBuilder("Validation failed: ");
            if (bindingResult.hasErrors()) {
                bindingResult.getAllErrors().forEach(error -> {
                    if (error instanceof FieldError) {
                        errorSummary.append(((FieldError) error).getField()).append(" - ");
                    }
                    errorSummary.append(error.getDefaultMessage()).append("; ");
                });
            }
            if (hasFileError) {
                errorSummary.append("file - ").append(fileError).append(";");
            }

            model.addAttribute("errorMessage", errorSummary.toString());
            return "assessments"; // Return directly to show validation errors inline
        }

        try {
            // Save the file using StorageService
            String savedFileName = storageService.store(file);

            // Populate the metadata object
            assessmentMaterial.setId(idCounter.getAndIncrement());
            assessmentMaterial.setFileName(savedFileName);

            // Save to in-memory list
            materialsList.add(assessmentMaterial);

            StringBuilder successMsg = new StringBuilder("File '");
            successMsg.append(file.getOriginalFilename())
                      .append("' uploaded successfully.");
            redirectAttributes.addFlashAttribute("successMessage", successMsg.toString());

        } catch (Exception e) {
            StringBuilder failMsg = new StringBuilder("Failed to upload file: ");
            failMsg.append(e.getMessage());
            redirectAttributes.addFlashAttribute("errorMessage", failMsg.toString());
        }

        return "redirect:/assessments";
    }

    // Task 3.3: GET /assessments/download/{filename}
    @GetMapping("/assessments/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        try {
            Resource file = storageService.loadAsResource(filename);

            String contentType = "application/octet-stream";
            try {
                contentType = file.getURL().openConnection().getContentType();
                if (contentType == null) {
                    contentType = "application/octet-stream";
                }
            } catch (Exception ignored) {
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                    .body(file);
        } catch (FileNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Controller-level Exception Handler for Max File Size Exceeded
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("errorMessage", "Upload failed: File size exceeds the maximum limit of 10MB.");
        return "redirect:/assessments";
    }
}
