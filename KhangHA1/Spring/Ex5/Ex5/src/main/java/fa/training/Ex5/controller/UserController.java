package fa.training.TrainingMaterialManagementSystem.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import fa.training.TrainingMaterialManagementSystem.entity.Material;
import fa.training.TrainingMaterialManagementSystem.service.MaterialService;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/materials")
@RequiredArgsConstructor
public class UserController {

        private final MaterialService materialService;

        @PostMapping("/upload")
        public String upload(
                        @RequestParam Long subjectId,
                        @RequestParam String description,
                        @RequestParam MultipartFile file,
                        RedirectAttributes ra) {

                materialService.upload(subjectId, file, description);

                ra.addFlashAttribute(
                                "success",
                                "Material uploaded successfully.");

                return "redirect:/materials/" + subjectId;
        }

        @GetMapping("/download/{id}")
        public ResponseEntity<Resource> download(@PathVariable Long id) {

                Material material = materialService.findById(id)
                                .orElseThrow();

                Resource resource = materialService.download(id);

                return ResponseEntity.ok()
                                .header(
                                                HttpHeaders.CONTENT_DISPOSITION,
                                                "attachment; filename=\""
                                                                + material.getFileName()
                                                                + "\"")
                                .body(resource);
        }

        @PostMapping("/delete/{id}")
        public String delete(
                        @PathVariable Long id,
                        RedirectAttributes ra) {

                materialService.deleteMaterial(id);

                ra.addFlashAttribute(
                                "success",
                                "Deleted successfully");

                return "redirect:/subjects";
        }
}
