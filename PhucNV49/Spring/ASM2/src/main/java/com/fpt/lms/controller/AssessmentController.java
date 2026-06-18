package com.fpt.lms.controller;

import com.fpt.lms.model.AssessmentMaterial;
import com.fpt.lms.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
public class AssessmentController {

    private final StorageService storageService;

    private final List<AssessmentMaterial> materials = new ArrayList<>();

    private long idCounter = 1;

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/assessments")
    public String listAssessments(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    @PostMapping("/assessments/upload")
    public String uploadMaterial(@RequestParam String title,
                                 @RequestParam String description,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        try {
            String filename = storageService.store(file);
            materials.add(new AssessmentMaterial(idCounter++, title, description, filename));
            redirectAttributes.addFlashAttribute("successMessage", "File uploaded successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to upload file: " + e.getMessage());
        }
        return "redirect:/assessments";
    }

    @GetMapping("/assessments/download/{filename}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable String filename) {
        try {
            Resource resource = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
