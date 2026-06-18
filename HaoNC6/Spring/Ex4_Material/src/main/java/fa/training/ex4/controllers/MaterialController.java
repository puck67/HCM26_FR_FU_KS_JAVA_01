package fa.training.ex4.controllers;

import fa.training.ex4.services.MaterialService;
import fa.training.ex4.services.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping("/materials")
@RequiredArgsConstructor
public class MaterialController {
    private final MaterialService materialService;
    private final SubjectService subjectService;

    @GetMapping("/subject/{subjectId}")
    public String manageMaterials(@PathVariable Long subjectId, Model model) {
        model.addAttribute("subject", subjectService.findById(subjectId));
        model.addAttribute("materials", materialService.findBySubjectId(subjectId));
        model.addAttribute("pageTitle", "Material Management");
        return "material/manage";
    }

    @PostMapping("/upload")
    public String uploadMaterial(@RequestParam("subjectId") Long subjectId,
                                 @RequestParam("description") String description,
                                 @RequestParam("file") MultipartFile file,
                                 RedirectAttributes redirectAttributes) {
        materialService.uploadMaterial(subjectId, description, file);
        redirectAttributes.addFlashAttribute("successMessage", "Material uploaded successfully.");
        return "redirect:/materials/subject/" + subjectId;
    }

    @GetMapping("/download/{id}")
    @ResponseBody
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long id) throws UnsupportedEncodingException {
        StringBuilder originalFileName = new StringBuilder();
        Resource resource = materialService.downloadMaterial(id, originalFileName);

        String encodedFileName = URLEncoder.encode(originalFileName.toString(), StandardCharsets.UTF_8.toString())
                .replaceAll("\\+", "%20");

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                .body(resource);
    }

    @GetMapping("/delete/{id}")
    public String deleteMaterial(@PathVariable Long id, @RequestParam("subjectId") Long subjectId) {
        materialService.deleteMaterial(id);
        return "redirect:/materials/subject/" + subjectId;
    }
}
