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

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storage;
    private final List<AssessmentMaterial> uploadedItems = Collections.synchronizedList(new ArrayList<>());
    private final AtomicLong nextId = new AtomicLong(1);

    public AssessmentController(StorageService storage) {
        this.storage = storage;
    }

    @GetMapping
    public String listAssessments(Model m) {
        List<AssessmentMaterial> listDesc = new ArrayList<>();
        for (int i = uploadedItems.size() - 1; i >= 0; i--) {
            listDesc.add(uploadedItems.get(i));
        }
        
        m.addAttribute("materials", listDesc);
        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadAssessment(@RequestParam("title") String txtTitle,
                                   @RequestParam("description") String txtDesc,
                                   @RequestParam("file") MultipartFile fileData,
                                   RedirectAttributes redirAttrs) {
        if (txtTitle == null || "".equals(txtTitle.trim())) {
            redirAttrs.addFlashAttribute("errorMessage", "Title cannot be empty!");
            return "redirect:/assessments";
        }
        if (fileData == null || fileData.isEmpty()) {
            redirAttrs.addFlashAttribute("errorMessage", "Please select a file to upload!");
            return "redirect:/assessments";
        }

        try {
            String savedFileName = storage.store(fileData);

            AssessmentMaterial mItem = new AssessmentMaterial(
                    nextId.getAndIncrement(),
                    txtTitle.trim(),
                    txtDesc == null ? "" : txtDesc.trim(),
                    savedFileName
            );
            uploadedItems.add(mItem);

            redirAttrs.addFlashAttribute("successMessage", "File uploaded successfully: " + savedFileName);
        } catch (StorageException ex) {
            redirAttrs.addFlashAttribute("errorMessage", "Upload failed: " + ex.getMessage());
        }

        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable("filename") String fName) {
        try {
            Resource resource = storage.loadAsResource(fName);
            
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (StorageException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
