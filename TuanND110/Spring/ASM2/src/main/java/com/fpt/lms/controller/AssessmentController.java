package com.fpt.lms.controller;

import com.fpt.lms.model.AssessmentMaterial;
import com.fpt.lms.service.AssessmentMaterialService;
import com.fpt.lms.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class AssessmentController {

    private final StorageService storageService;
    private final AssessmentMaterialService assessmentMaterialService;

    @GetMapping("/assessments")
    public String listAssessments(Model model) {
        List<AssessmentMaterial> materials = assessmentMaterialService.findAll();
        log.info("Fetching all assessment materials. Found {} entries.", materials.size());
        model.addAttribute("materials", materials);
        return "assessments";
    }

    @PostMapping("/assessments/upload")
    public String uploadMaterial(@RequestParam String title,
                                 @RequestParam String description,
                                 RedirectAttributes redirectAttributes) {
        log.info("Attempting to create assessment material. Title: {}", title);
        try {
            AssessmentMaterial material = AssessmentMaterial.builder()
                    .title(title)
                    .description(description)
                    .fileName("")
                    .build();
            AssessmentMaterial saved = assessmentMaterialService.save(material);
            log.info("Successfully saved assessment material to database: {}", saved);
            redirectAttributes.addFlashAttribute("successMessage", "Assessment material created successfully!");
        } catch (Exception e) {
            log.error("Failed to create assessment material: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to create assessment material: " + e.getMessage());
        }
        return "redirect:/assessments";
    }

    @GetMapping("/assessments/download/{filename}")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable String filename) {
        log.info("Request download for file: {}", filename);
        try {
            Resource resource = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (Exception e) {
            log.error("Failed to download file '{}': {}", filename, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/assessments/template")
    public ResponseEntity<byte[]> downloadTemplate() {
        log.info("Downloading CSV template for assessment materials.");
        String content = "Title;Description;File Name\n" +
                "Final Exam;Final term examination;final_exam.pdf\n" +
                "Quiz 1;Introduction Quiz;quiz1.txt\n";
        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"assessment_template.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @GetMapping("/assessments/export")
    public ResponseEntity<byte[]> exportAssessments() {
        log.info("Exporting all assessment materials to CSV.");
        StringBuilder content = new StringBuilder("Title;Description;File Name\n");
        List<AssessmentMaterial> materials = assessmentMaterialService.findAll();
        for (AssessmentMaterial m : materials) {
            content.append(m.getTitle() != null ? m.getTitle() : "").append(";")
                   .append(m.getDescription() != null ? m.getDescription() : "").append(";")
                   .append(m.getFileName() != null ? m.getFileName() : "").append("\n");
        }

        byte[] bom = new byte[] { (byte)0xEF, (byte)0xBB, (byte)0xBF };
        byte[] contentBytes = content.toString().getBytes(StandardCharsets.UTF_8);
        byte[] result = new byte[bom.length + contentBytes.length];
        System.arraycopy(bom, 0, result, 0, bom.length);
        System.arraycopy(contentBytes, 0, result, bom.length, contentBytes.length);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"assessments_export.csv\"")
                .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                .body(result);
    }

    @PostMapping("/assessments/import")
    public String importAssessments(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        log.info("CSV Import requested for assessment materials. File: {}, Size: {} bytes", file.getOriginalFilename(), file.getSize());
        if (file.isEmpty()) {
            log.warn("Import failed: Uploaded file is empty.");
            redirectAttributes.addFlashAttribute("errorMessage", "Failed to import: Selected file is empty.");
            return "redirect:/assessments";
        }

        int successCount = 0;
        int failedCount = 0;

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            String line;
            boolean firstLine = true;
            while ((line = reader.readLine()) != null) {
                if (firstLine) {
                    firstLine = false;
                    log.info("Skipping header row: {}", line);
                    continue;
                }
                
                if (line.trim().isEmpty()) {
                    continue;
                }

                log.info("Processing row: {}", line);
                String[] data = line.split(";", -1);
                if (data.length >= 3) {
                    try {
                        AssessmentMaterial material = AssessmentMaterial.builder()
                                .title(data[0].trim())
                                .description(data[1].trim())
                                .fileName(data[2].trim())
                                .build();
                        AssessmentMaterial saved = assessmentMaterialService.save(material);
                        log.info("Saved imported material to database: {}", saved);
                        successCount++;
                    } catch (Exception ex) {
                        log.error("Failed to parse row: '{}' due to error: {}", line, ex.getMessage());
                        failedCount++;
                    }
                } else {
                    log.warn("Skipping row: '{}' - expected at least 3 columns, got {}", line, data.length);
                    failedCount++;
                }
            }

            if (failedCount > 0) {
                log.warn("Import finished with errors. Success: {}, Failed: {}", successCount, failedCount);
                redirectAttributes.addFlashAttribute("errorMessage", "Imported " + successCount + " materials. Failed to import " + failedCount + " rows due to invalid data format.");
            } else {
                log.info("Import completed successfully. Imported {} materials.", successCount);
                redirectAttributes.addFlashAttribute("successMessage", "All " + successCount + " materials imported and saved to database successfully!");
            }
        } catch (Exception e) {
            log.error("CSV Import failed due to general exception: {}", e.getMessage(), e);
            redirectAttributes.addFlashAttribute("errorMessage", "Error parsing CSV file: " + e.getMessage());
        }

        return "redirect:/assessments";
    }
}
