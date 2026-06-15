package org.example.api.controller;

import org.example.api.exception.StorageException;
import org.example.api.exception.StorageFileNotFoundException;
import org.example.api.model.AssessmentMaterial;
import org.example.api.service.StorageService;
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

/**
 * Controller for assessment material upload, listing, and download.
 * Task 3: Handles all three required endpoints.
 */
@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    /**
     * In-memory store for assessment materials (no database required).
     */
    private final List<AssessmentMaterial> materials = new ArrayList<>();

    /**
     * Thread-safe ID generator.
     */
    private final AtomicLong idCounter = new AtomicLong(1);

    private final StorageService storageService;

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task 3 – Endpoint 1: GET /assessments
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Displays the upload form and the list of all previously uploaded materials.
     */
    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task 3 – Endpoint 2: POST /assessments/upload
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Handles the multipart form submission.
     * Saves the file via StorageService, then stores the metadata in-memory.
     * Task 6: If an error occurs, a flash message is added via RedirectAttributes.
     */
    @PostMapping("/upload")
    public String uploadAssessment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        // Basic validation
        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Title must not be empty.");
            return "redirect:/assessments";
        }
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/assessments";
        }

        try {
            String storedFilename = storageService.store(file);

            AssessmentMaterial material = AssessmentMaterial.builder()
                    .id(idCounter.getAndIncrement())
                    .title(title.trim())
                    .description(description != null ? description.trim() : "")
                    .fileName(storedFilename)
                    .build();

            materials.add(material);
            redirectAttributes.addFlashAttribute(
                    "successMessage",
                    "File \"" + file.getOriginalFilename() + "\" uploaded successfully!");

        } catch (StorageException e) {
            // Task 6 – Error handling: pass error message to the redirected page
            redirectAttributes.addFlashAttribute(
                    "errorMessage",
                    "Upload failed: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Task 3 – Endpoint 3: GET /assessments/download/{filename}
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Serves an uploaded file for download.
     *
     * @param filename the stored filename (from the database / in-memory list)
     */
    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (StorageFileNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
