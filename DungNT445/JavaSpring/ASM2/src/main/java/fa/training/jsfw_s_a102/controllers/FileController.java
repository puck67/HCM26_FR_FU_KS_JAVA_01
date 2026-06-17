package fa.training.jsfw_s_a102.controllers;

import fa.training.jsfw_s_a102.services.FileStorageService;
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

@Controller
@RequiredArgsConstructor
public class FileController {
    private final FileStorageService fileStorageService;

    @GetMapping("/")
    public String listUploadFiles(Model model) {
        model.addAttribute("files", fileStorageService.loadAllFiles());
        return "uploadForm";
    }

    @PostMapping("/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file, RedirectAttributes redirectAttributes) {
        try {
            String contentType = file.getContentType();
            if (contentType == null || (!contentType.startsWith("image/") &&
                    !contentType.equals("application/pdf") &&
                    !contentType.equals("text/plain"))) {
                redirectAttributes.addFlashAttribute("error", "Định dạng file không được hỗ trợ! Chỉ nhận ảnh, PDF hoặc TXT.");
                return "redirect:/";
            }

            fileStorageService.storeFile(file);

            redirectAttributes.addFlashAttribute("message", "Bạn đã upload file thành công: " + file.getOriginalFilename());
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Lỗi: " + ex.getMessage());
        }
        return "redirect:/";
    }

    @GetMapping("/download/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            Resource resource = fileStorageService.loadFileAsResource(filename);

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
