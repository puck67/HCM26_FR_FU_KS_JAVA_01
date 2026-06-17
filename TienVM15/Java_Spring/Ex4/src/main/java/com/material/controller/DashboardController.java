package com.material.controller;

import com.material.repository.MaterialRepository;
import com.material.repository.SubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class DashboardController {

    private final SubjectRepository subjectRepository;
    private final MaterialRepository materialRepository;

    @GetMapping("/")
    public String dashboard(Model model) {
        long totalSubjects = subjectRepository.count();
        long totalMaterials = materialRepository.count();
        Long totalBytes = materialRepository.getTotalStorageUsed();
        if (totalBytes == null) {
            totalBytes = 0L;
        }

        model.addAttribute("totalSubjects", totalSubjects);
        model.addAttribute("totalMaterials", totalMaterials);
        model.addAttribute("formattedStorage", formatStorageSize(totalBytes));

        return "dashboard";
    }

    private String formatStorageSize(long bytes) {
        if (bytes <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(bytes) / Math.log10(1024));
        return new java.text.DecimalFormat("#,##0.#").format(bytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }
}
