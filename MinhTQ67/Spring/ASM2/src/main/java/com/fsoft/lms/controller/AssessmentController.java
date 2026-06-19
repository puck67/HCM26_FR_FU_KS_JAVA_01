package com.fsoft.lms.controller;

import com.fsoft.lms.model.AssessmentMaterial;
import com.fsoft.lms.service.StorageException;
import com.fsoft.lms.service.StorageFileNotFoundException;
import com.fsoft.lms.service.StorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class AssessmentController {

    private final StorageService storageService;

    // In-memory list to store metadata about uploaded materials
    private final List<AssessmentMaterial> materials = new ArrayList<>();

    // Simple counter to generate ids
    private final AtomicLong idCounter = new AtomicLong(0);

    @Autowired
    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void init() {
        storageService.init();
    }

    /**
     * GET /assessments
     * Retrieve the list of all AssessmentMaterial objects and display the main upload page.
     */
    @GetMapping("/assessments")
    public String listMaterials(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    /**
     * POST /assessments/upload
     * Accepts a MultipartFile plus title and description fields, stores the file,
     * saves the metadata in the in-memory list, and redirects back to /assessments.
     */
    @PostMapping("/assessments/upload")
    public String handleFileUpload(@RequestParam("title") String title,
                                    @RequestParam("description") String description,
                                    @RequestParam("file") MultipartFile file,
                                    RedirectAttributes redirectAttributes) {
        try {
            String storedFileName = storageService.store(file);

            AssessmentMaterial material = new AssessmentMaterial(
                    idCounter.incrementAndGet(),
                    title,
                    description,
                    storedFileName
            );
            materials.add(material);

            redirectAttributes.addFlashAttribute("message",
                    "File uploaded successfully: " + (file.getOriginalFilename()));
        } catch (StorageException e) {
            redirectAttributes.addFlashAttribute("message",
                    "Failed to upload file: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    /**
     * GET /assessments/download/{filename}
     * Serves the uploaded files for download by filename.
     */
    @GetMapping("/assessments/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    /**
     * Handle the case where the requested file is not found on disk.
     */
    @ExceptionHandler(StorageFileNotFoundException.class)
    @ResponseStatus(org.springframework.http.HttpStatus.NOT_FOUND)
    public String handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return "redirect:/assessments";
    }
}
