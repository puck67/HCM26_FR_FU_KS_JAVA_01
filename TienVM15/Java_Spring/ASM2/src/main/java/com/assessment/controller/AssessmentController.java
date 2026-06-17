package com.assessment.controller;

import com.assessment.exception.StorageException;
import com.assessment.model.AssessmentMaterial;
import com.assessment.service.AssessmentService;
import com.assessment.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/assessments")
@RequiredArgsConstructor
public class AssessmentController {

    private final AssessmentService assessmentService;
    private final StorageService storageService;

    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("materials", assessmentService.getAllMaterials());
        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadAssessment(@RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Title is required.");
            return "redirect:/assessments";
        }
        if (file.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select a file to upload.");
            return "redirect:/assessments";
        }

        try {
            String storedFileName = storageService.store(file);
            AssessmentMaterial material = AssessmentMaterial.builder()
                    .title(title)
                    .description(description)
                    .fileName(storedFileName)
                    .build();
            assessmentService.addMaterial(material);
            redirectAttributes.addFlashAttribute("successMessage", "Material uploaded successfully!");
        } catch (StorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Upload failed: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename);
        
        // Clean prefix UUID from filename for the client download name
        String displayFilename = filename;
        if (filename.contains("-")) {
            displayFilename = filename.substring(filename.indexOf("-") + 1);
        }

        String encodedFilename;
        try {
            encodedFilename = URLEncoder.encode(displayFilename, StandardCharsets.UTF_8.toString()).replace("+", "%20");
        } catch (UnsupportedEncodingException e) {
            encodedFilename = displayFilename;
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + displayFilename + "\"; filename*=UTF-8''" + encodedFilename)
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteAssessment(@PathVariable long id, RedirectAttributes redirectAttributes) {
        try {
            AssessmentMaterial material = assessmentService.getAllMaterials().stream()
                    .filter(m -> m.getId() == id)
                    .findFirst()
                    .orElse(null);
            
            if (material != null) {
                storageService.delete(material.getFileName());
                assessmentService.deleteMaterial(id);
                redirectAttributes.addFlashAttribute("successMessage", "Material deleted successfully!");
            } else {
                redirectAttributes.addFlashAttribute("errorMessage", "Material not found.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Delete failed: " + e.getMessage());
        }
        return "redirect:/assessments";
    }
}
