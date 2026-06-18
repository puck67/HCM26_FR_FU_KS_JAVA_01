package fa.training.lms.controller;



import fa.training.lms.model.AssessmentMaterial;
import fa.training.lms.service.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    @Autowired
    private StorageService storageService;

    private final List<AssessmentMaterial>
            materials = new ArrayList<>();

    private final AtomicLong counter =
            new AtomicLong(1);

    @GetMapping
    public String showPage(Model model) {

        model.addAttribute(
                "materials",
                materials
        );

        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadFile(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {

        try {

            String fileName =
                    storageService.store(file);

            AssessmentMaterial material =
                    new AssessmentMaterial(
                            counter.getAndIncrement(),
                            title,
                            description,
                            fileName
                    );

            materials.add(material);

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Upload successful!"
            );

        } catch (Exception e) {

            redirectAttributes.addFlashAttribute(
                    "message",
                    "Upload failed: " + e.getMessage()
            );
        }

        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(
            @PathVariable String filename) {

        Resource resource =
                storageService.loadAsResource(filename);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" +
                                resource.getFilename() +
                                "\"")
                .body(resource);
    }
}