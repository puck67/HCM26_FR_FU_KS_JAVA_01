package com.example.Ex4.controller;

import com.example.Ex4.repository.MaterialRepository;
import com.example.Ex4.repository.SubjectRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;

    public DashboardController(SubjectRepository subjectRepository, MaterialRepository materialRepository) {
        this.subjectRepository = subjectRepository;
        this.materialRepository = materialRepository;
    }

    @GetMapping({"/","/dashboard"})
    public String dashboard(Model model){
        long totalSubjects = subjectRepository.count();
        long totalMaterials = materialRepository.count();
        long totalSize = materialRepository.findAll().stream()
                .filter(m -> m.getFileSize() != null)
                .mapToLong(com.example.Ex4.entity.Material::getFileSize)
                .sum();

        model.addAttribute("totalSubjects", totalSubjects);
        model.addAttribute("totalMaterials", totalMaterials);
        model.addAttribute("totalSize", totalSize);
        // return layout/main so layout's fragment displays dashboard when attributes exist
        return "layout/main";
    }
}
