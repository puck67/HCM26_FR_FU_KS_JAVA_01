package com.material.controller;

import com.material.entity.Material;
import com.material.entity.Subject;
import com.material.exception.StorageException;
import com.material.repository.MaterialRepository;
import com.material.repository.SubjectRepository;
import com.material.service.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;
    private final StorageService storageService;

    @GetMapping
    public String viewMaterials(@RequestParam(value = "subjectId", required = false) Long subjectId,
                                @RequestParam(value = "query", required = false) String query,
                                Model model) {
        
        List<Subject> subjects = subjectRepository.findAll();
        model.addAttribute("subjects", subjects);

        if (query != null && !query.trim().isEmpty()) {
            // Search materials (Bonus 2)
            List<Material> searchResults = materialRepository.searchMaterials(query);
            model.addAttribute("searchResults", searchResults);
            model.addAttribute("searchQuery", query);
        } else if (subjectId != null) {
            Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
            if (subjectOpt.isPresent()) {
                Subject subject = subjectOpt.get();
                model.addAttribute("selectedSubject", subject);
                model.addAttribute("materials", materialRepository.findBySubjectId(subjectId));
            }
        } else if (!subjects.isEmpty()) {
            // Default to first subject if none selected
            Subject firstSubject = subjects.get(0);
            model.addAttribute("selectedSubject", firstSubject);
            model.addAttribute("materials", materialRepository.findBySubjectId(firstSubject.getId()));
        }

        return "material-mgmt";
    }

    @PostMapping("/upload")
    public String uploadMaterials(@RequestParam("subjectId") Long subjectId,
                                  @RequestParam("description") String description,
                                  @RequestParam("category") String category,
                                  @RequestParam("files") MultipartFile[] files,
                                  RedirectAttributes redirectAttributes) {
        
        Optional<Subject> subjectOpt = subjectRepository.findById(subjectId);
        if (subjectOpt.isEmpty()) {
            return "redirect:/materials";
        }
        Subject subject = subjectOpt.get();

        if (files == null || files.length == 0 || (files.length == 1 && files[0].isEmpty())) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select at least one file to upload.");
            return "redirect:/materials?subjectId=" + subjectId;
        }

        try {
            int successCount = 0;
            for (MultipartFile file : files) {
                if (file.isEmpty()) continue;

                // Validate and store physically
                String storedName = storageService.store(file);

                // Save metadata
                Material material = Material.builder()
                        .fileName(file.getOriginalFilename())
                        .storedFileName(storedName)
                        .fileSize(file.getSize())
                        .fileType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                        .uploadDate(LocalDateTime.now())
                        .description(description)
                        .category(category)
                        .subject(subject)
                        .build();

                materialRepository.save(material);
                successCount++;
            }
            if (successCount > 0) {
                redirectAttributes.addFlashAttribute("successMessage", "Material uploaded successfully. (" + successCount + " file(s))");
            }
        } catch (StorageException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/materials?subjectId=" + subjectId;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable("id") Long id) {
        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new StorageException("Material not found with id: " + id));

        Resource resource = storageService.loadAsResource(material.getStoredFileName());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(material.getFileSize()))
                .contentType(MediaType.parseMediaType(material.getFileType()))
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteMaterial(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        Optional<Material> materialOpt = materialRepository.findById(id);
        if (materialOpt.isPresent()) {
            Material material = materialOpt.get();
            Long subjectId = material.getSubject().getId();

            // 1. Delete physically
            storageService.delete(material.getStoredFileName());

            // 2. Delete from DB
            materialRepository.deleteById(id);

            redirectAttributes.addFlashAttribute("successMessage", "Material deleted successfully.");
            return "redirect:/materials?subjectId=" + subjectId;
        }
        return "redirect:/materials";
    }
}
