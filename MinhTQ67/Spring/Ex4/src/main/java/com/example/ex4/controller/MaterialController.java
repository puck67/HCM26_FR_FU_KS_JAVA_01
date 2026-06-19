package com.example.ex4.controller;

import com.example.ex4.entity.Category;
import com.example.ex4.entity.Material;
import com.example.ex4.entity.Subject;
import com.example.ex4.repository.MaterialRepository;
import com.example.ex4.repository.SubjectRepository;
import com.example.ex4.service.FileStorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/materials")
public class MaterialController {

    private final MaterialRepository materialRepository;
    private final SubjectRepository subjectRepository;
    private final FileStorageService fileStorageService;

    public MaterialController(MaterialRepository materialRepository, SubjectRepository subjectRepository, FileStorageService fileStorageService) {
        this.materialRepository = materialRepository;
        this.subjectRepository = subjectRepository;
        this.fileStorageService = fileStorageService;
    }

    @GetMapping
    public String listMaterials(@RequestParam(required = false) Long subjectId, 
                                @RequestParam(required = false) String keyword, 
                                Model model) {
        List<Material> materials;
        if (keyword != null && !keyword.isEmpty()) {
            materials = materialRepository.searchMaterials(keyword);
            model.addAttribute("keyword", keyword);
        } else if (subjectId != null) {
            materials = materialRepository.findBySubject_SubjectId(subjectId);
            Subject subject = subjectRepository.findById(subjectId).orElseThrow();
            model.addAttribute("currentSubject", subject);
        } else {
            materials = materialRepository.findAll();
        }
        
        model.addAttribute("materials", materials);
        model.addAttribute("subjects", subjectRepository.findAll());
        model.addAttribute("categories", Category.values());
        
        return "materials/list";
    }

    @PostMapping("/upload")
    public String uploadMaterials(@RequestParam("files") MultipartFile[] files,
                                  @RequestParam("description") String description,
                                  @RequestParam("categoryId") String categoryId,
                                  @RequestParam("subjectId") Long subjectId,
                                  RedirectAttributes redirectAttributes) {
        
        Subject subject = subjectRepository.findById(subjectId).orElseThrow();
        Category category = Category.valueOf(categoryId);

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                String storedFileName = fileStorageService.storeFile(file);
                
                Material material = new Material();
                material.setFileName(file.getOriginalFilename());
                material.setStoredFileName(storedFileName);
                material.setFileSize(file.getSize());
                material.setFileType(file.getContentType());
                material.setDescription(description);
                material.setCategory(category);
                material.setSubject(subject);
                
                materialRepository.save(material);
            }
        }
        
        redirectAttributes.addFlashAttribute("successMessage", "Materials uploaded successfully!");
        return "redirect:/materials?subjectId=" + subjectId;
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> downloadFile(@PathVariable Long id) {
        Material material = materialRepository.findById(id).orElseThrow();
        Resource resource = fileStorageService.loadFileAsResource(material.getStoredFileName());
        
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getFileName() + "\"")
                .body(resource);
    }
    
    @GetMapping("/delete/{id}")
    public String deleteMaterial(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Material material = materialRepository.findById(id).orElseThrow();
        Long subjectId = material.getSubject().getSubjectId();
        
        fileStorageService.deleteFile(material.getStoredFileName());
        materialRepository.delete(material);
        
        redirectAttributes.addFlashAttribute("successMessage", "Material deleted successfully!");
        return "redirect:/materials?subjectId=" + subjectId;
    }
}
