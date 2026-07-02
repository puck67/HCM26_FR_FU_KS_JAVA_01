package com.lms.materialmanager.controller;

import com.lms.materialmanager.service.MaterialService;
import com.lms.materialmanager.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final SubjectService subjectService;
    private final MaterialService materialService;

    @Autowired
    public DashboardController(SubjectService subjectService, MaterialService materialService) {
        this.subjectService = subjectService;
        this.materialService = materialService;
    }

    @GetMapping({"/", "/dashboard"})
    public String viewDashboard(Model model) {
        long totalSubjects = subjectService.getAllSubjects().size();
        long totalMaterials = materialService.getTotalMaterialsCount();
        long totalBytes = materialService.getTotalStorageSize();
        String totalStorageFormatted = formatSize(totalBytes);

        model.addAttribute("totalSubjects", totalSubjects);
        model.addAttribute("totalMaterials", totalMaterials);
        model.addAttribute("totalStorage", totalStorageFormatted);
        model.addAttribute("activePage", "dashboard");

        return "dashboard";
    }

    private String formatSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = 0;
        double size = bytes;
        while (size >= 1024 && digitGroups < units.length - 1) {
            size /= 1024;
            digitGroups++;
        }
        return String.format("%.1f %s", size, units[digitGroups]);
    }
}
