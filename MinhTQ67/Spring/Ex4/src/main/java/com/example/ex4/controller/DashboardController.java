package com.example.ex4.controller;

import com.example.ex4.repository.MaterialRepository;
import com.example.ex4.repository.SubjectRepository;
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

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalSubjects = subjectRepository.count();
        long totalMaterials = materialRepository.count();
        Long totalStorageBytes = materialRepository.getTotalStorageUsed();
        
        String storageUsed = formatSize(totalStorageBytes != null ? totalStorageBytes : 0L);
        
        model.addAttribute("totalSubjects", totalSubjects);
        model.addAttribute("totalMaterials", totalMaterials);
        model.addAttribute("storageUsed", storageUsed);
        
        return "dashboard";
    }
    
    private String formatSize(long v) {
        if (v < 1024) return v + " B";
        int z = (63 - Long.numberOfLeadingZeros(v)) / 10;
        return String.format("%.1f %sB", (double)v / (1L << (z*10)), " KMGTPE".charAt(z));
    }
}
