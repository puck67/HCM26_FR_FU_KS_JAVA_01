package com.example.Ex4.controller;

import com.example.Ex4.controller.base.GenericController;
import com.example.Ex4.entity.Material;
import com.example.Ex4.entity.Subject;
import com.example.Ex4.service.MaterialService;
import com.example.Ex4.service.SubjectService;
import org.springframework.core.io.Resource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;

@Controller
@RequestMapping("/materials")
public class MaterialController extends GenericController<Material> {

    private final MaterialService materialService;
    private final SubjectService subjectService;

    public MaterialController(MaterialService materialService, SubjectService subjectService) {
        super(materialService, "material", "/materials");
        this.materialService = materialService;
        this.subjectService = subjectService;
    }

    @ModelAttribute("subjects")
    public List<Subject> populateSubjects() {
        return subjectService.findAll();
    }

    @PostMapping("/upload")
    public String upload(@ModelAttribute Material material,
                         @RequestParam MultipartFile file) throws Exception {
        materialService.upload(material, file);
        if (material.getSubject() != null && material.getSubject().getId() != null) {
            return "redirect:/subjects/" + material.getSubject().getId();
        }
        return "redirect:/materials";
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable Long id) throws Exception {
        Material material = materialService.findById(id);
        Path path = materialService.loadFile(material.getStoredFileName());
        Resource resource = new FileSystemResource(path);

        return ResponseEntity.ok()
                .header("Content-Disposition",
                        "attachment; filename=\"" + material.getFileName() + "\"")
                .body(resource);
    }

    @Override
    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        Long subjectId = null;
        Material material = materialService.findById(id);
        if (material != null && material.getSubject() != null) {
            subjectId = material.getSubject().getId();
        }
        materialService.delete(id);
        if (subjectId != null) {
            return "redirect:/subjects/" + subjectId;
        }
        return "redirect:/materials";
    }
}