package org.example.jsfw_s_a102.controller;

import org.example.jsfw_s_a102.model.AssessmentMaterial;
import org.example.jsfw_s_a102.service.StorageService;
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
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storageService;
    private final List<AssessmentMaterial> materials = new ArrayList<>();
    private final AtomicLong counter = new AtomicLong();

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadFile(@RequestParam("title") String title,
                             @RequestParam("description") String description,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            storageService.store(file);
            AssessmentMaterial material = new AssessmentMaterial(
                    counter.incrementAndGet(),
                    title,
                    description,
                    file.getOriginalFilename()
            );
            materials.add(material);
            redirectAttributes.addFlashAttribute("message", "File uploaded successfully: " + file.getOriginalFilename());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to upload file: " + e.getMessage());
        }
        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
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
