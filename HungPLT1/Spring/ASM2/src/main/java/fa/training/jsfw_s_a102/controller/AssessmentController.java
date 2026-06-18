package fa.training.jsfw_s_a102.controller;

import fa.training.jsfw_s_a102.model.AssessmentMaterial;
import fa.training.jsfw_s_a102.service.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storageService;
    private final List<AssessmentMaterial> materials = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(1);

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    @PostMapping("/upload")
    public String uploadAssessment(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("file") MultipartFile file,
            RedirectAttributes redirectAttributes) {
        try {
            if (file.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Please select a file to upload.");
                return "redirect:/assessments";
            }
            String fileName = storageService.store(file);
            AssessmentMaterial material = new AssessmentMaterial(
                    idCounter.getAndIncrement(),
                    title,
                    description,
                    fileName
            );
            materials.add(material);
            redirectAttributes.addFlashAttribute("message",
                    "Assessment material \"" + title + "\" uploaded successfully!");
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error",
                    "File upload failed: " + ex.getMessage());
        }
        return "redirect:/assessments";
    }

    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = storageService.loadAsResource(filename);
            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
