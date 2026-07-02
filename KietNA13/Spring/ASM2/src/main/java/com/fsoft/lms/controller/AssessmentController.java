package com.fsoft.lms.controller;

import com.fsoft.lms.model.AssessmentMaterial;
import com.fsoft.lms.service.StorageException;
import com.fsoft.lms.service.StorageFileNotFoundException;
import com.fsoft.lms.service.StorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Controller xử lý upload/download tài liệu Assessment.
 *
 * - In-memory list dùng CopyOnWriteArrayList để an toàn khi nhiều request đồng thời.
 * - Áp dụng PRG pattern: sau upload luôn redirect để tránh double-submit.
 */
@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    private static final Logger log = LoggerFactory.getLogger(AssessmentController.class);

    private final StorageService storageService;

    /**
     * Thread-safe list: phù hợp khi read nhiều hơn write (danh sách hiển thị).
     * Thay ArrayList thường để tránh ConcurrentModificationException dưới tải cao.
     */
    private final List<AssessmentMaterial> materials = new CopyOnWriteArrayList<>();
    private final AtomicLong idCounter = new AtomicLong(0);

    public AssessmentController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostConstruct
    public void initStorageOnStartup() {
        storageService.initStorage();
    }

    // ─────────────────────────────────────────────────────────────────────────

    /** GET /assessments — Hiển thị danh sách tài liệu và form upload */
    @GetMapping
    public String listAllMaterials(Model model) {
        model.addAttribute("materials", materials);
        return "assessments";
    }

    /**
     * POST /assessments/upload — Nhận file upload, lưu vào storage, thêm metadata vào list.
     * Validate title/description không blank trước khi lưu để tránh dữ liệu rác.
     */
    @PostMapping("/upload")
    public String addUploadedMaterial(@RequestParam("title") String title,
                                      @RequestParam("description") String description,
                                      @RequestParam("file") MultipartFile file,
                                      RedirectAttributes redirectAttributes) {

        if (title == null || title.isBlank()) {
            redirectAttributes.addFlashAttribute("message", "Upload failed: title must not be blank.");
            return "redirect:/assessments";
        }
        if (file == null || file.isEmpty()) {
            redirectAttributes.addFlashAttribute("message", "Upload failed: please select a file.");
            return "redirect:/assessments";
        }

        try {
            String storedFileName = storageService.saveUploadedFile(file);

            materials.add(new AssessmentMaterial(
                    idCounter.incrementAndGet(),
                    title.strip(),
                    description != null ? description.strip() : "",
                    storedFileName
            ));

            log.info("Material uploaded: title='{}', file='{}'", title, file.getOriginalFilename());
            redirectAttributes.addFlashAttribute("message",
                    "File uploaded successfully: " + file.getOriginalFilename());
        } catch (StorageException e) {
            log.error("Upload error for file '{}': {}", file.getOriginalFilename(), e.getMessage());
            redirectAttributes.addFlashAttribute("message",
                    "Upload failed: " + e.getMessage());
        }

        return "redirect:/assessments";
    }

    /**
     * GET /assessments/download/{filename} — Tải file theo tên lưu trên đĩa.
     * Content-Disposition được set để trình duyệt download thay vì hiển thị inline.
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadMaterialFile(@PathVariable String filename) {
        Resource resource = storageService.loadFileAsResource(filename);
        String contentDisposition = "attachment; filename=\"" + resource.getFilename() + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    /** Xử lý trường hợp file không tìm thấy — trả về 404 và redirect về danh sách */
    @ExceptionHandler(StorageFileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleFileNotFound(StorageFileNotFoundException ex) {
        log.warn("File not found: {}", ex.getMessage());
        return "redirect:/assessments";
    }
}
