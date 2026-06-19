package fa.training.JSFW_S_A102.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
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

import fa.training.JSFW_S_A102.model.AssessmentMaterial;
import fa.training.JSFW_S_A102.service.StorageService;

@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private final StorageService storageService;
    private final List<AssessmentMaterial> materials = new ArrayList<>();

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    /**
     * GET /assessments - Display upload form and list of all uploaded materials.
     */
    @GetMapping
    public String listAssessments(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    /**
     * POST /assessments/upload - Handle file upload with title and description.
     */
    @PostMapping("/upload")
    public String uploadAssessment(@RequestParam("title") String title,
                                   @RequestParam("description") String description,
                                   @RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {
        try {
            storageService.store(file);

            AssessmentMaterial material = new AssessmentMaterial(
                    title, description, file.getOriginalFilename());
            materials.add(material);

            redirectAttributes.addFlashAttribute("message",
                    "File uploaded successfully: " + file.getOriginalFilename());
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error",
                    "Failed to upload file: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    /**
     * GET /assessments/download/{filename} - Download a file by filename.
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        Resource resource = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
