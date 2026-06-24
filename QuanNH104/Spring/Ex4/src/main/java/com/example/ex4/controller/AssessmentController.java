package com.example.ex4.controller;

import com.example.ex4.model.AssessmentMaterial;
import com.example.ex4.repository.AssessmentMaterialRepository;
import com.example.ex4.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storageService;
    private final AssessmentMaterialRepository repository;

    public AssessmentController(StorageService storageService, AssessmentMaterialRepository repository) {
        this.storageService = storageService;
        this.repository = repository;
    }

    @GetMapping
    public String listAssessments(
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model) {

        List<AssessmentMaterial> materials;
        if (keyword != null && !keyword.trim().isEmpty()) {
            materials = repository.searchByKeyword(keyword.trim());
            model.addAttribute("keyword", keyword);
        } else {
            materials = repository.findAll();
        }

        long totalSubjects = repository.countDistinctSubjects();
        long totalMaterials = repository.count();
        long totalStorageBytes = repository.sumStorageUsed();

        model.addAttribute("materials", materials);
        model.addAttribute("totalSubjects", totalSubjects);
        model.addAttribute("totalMaterials", totalMaterials);
        model.addAttribute("totalStorage", formatSize(totalStorageBytes));

        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadAssessment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("subject") String subject,
            @RequestParam("category") String category,
            @RequestParam("files") MultipartFile[] files,
            RedirectAttributes redirectAttributes) {

        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Title is required!");
            return "redirect:/assessments";
        }
        if (subject == null || subject.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Subject is required!");
            return "redirect:/assessments";
        }
        if (files == null || files.length == 0 || (files.length == 1 && files[0].isEmpty())) {
            redirectAttributes.addFlashAttribute("error", "Please select at least one file to upload!");
            return "redirect:/assessments";
        }

        // Validate all files extension first
        for (MultipartFile file : files) {
            if (file.isEmpty()) continue;
            String originalName = file.getOriginalFilename();
            if (originalName == null || originalName.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Invalid file name!");
                return "redirect:/assessments";
            }
            String ext = getFileExtension(originalName).toLowerCase();
            List<String> allowed = Arrays.asList("pdf", "docx", "pptx", "zip", "txt");
            List<String> rejected = Arrays.asList("exe", "bat", "cmd", "sh");
            if (rejected.contains(ext) || !allowed.contains(ext)) {
                redirectAttributes.addFlashAttribute("error", "Unsupported file format: " + originalName);
                return "redirect:/assessments";
            }
        }

        try {
            int uploadCount = 0;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;
                String originalName = file.getOriginalFilename();
                long size = file.getSize();

                String storedName = storageService.store(file);

                AssessmentMaterial material = new AssessmentMaterial(
                        title.trim(),
                        description.trim(),
                        originalName,
                        storedName,
                        size,
                        LocalDateTime.now(),
                        subject.trim(),
                        category
                );

                repository.save(material);
                uploadCount++;
            }

            if (uploadCount > 0) {
                redirectAttributes.addFlashAttribute("message", "Uploaded " + uploadCount + " materials successfully.");
            } else {
                redirectAttributes.addFlashAttribute("error", "No files were uploaded.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Upload failed: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        try {
            AssessmentMaterial material = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Requested file does not exist."));

            Resource file = storageService.loadAsResource(material.getStoredName());
            
            String encodedFilename = URLEncoder.encode(material.getOriginalName(), StandardCharsets.UTF_8)
                    .replace("+", "%20");

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename*=UTF-8''" + encodedFilename)
                    .body(file);
        } catch (Exception e) {
            throw new RuntimeException("Requested file does not exist.");
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteMaterial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            AssessmentMaterial material = repository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Requested file does not exist."));

            storageService.delete(material.getStoredName());
            repository.delete(material);

            redirectAttributes.addFlashAttribute("message", "Material deleted successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete: " + e.getMessage());
        }
        return "redirect:/assessments";
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "0 Bytes";
        final String[] units = new String[] { "Bytes", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new java.text.DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    private String getFileExtension(String fileName) {
        int lastIndexOf = fileName.lastIndexOf(".");
        if (lastIndexOf == -1) {
            return "";
        }
        return fileName.substring(lastIndexOf + 1);
    }
}
