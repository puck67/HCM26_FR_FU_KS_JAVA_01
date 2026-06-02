package com.lms.assessment.controller;

import com.lms.assessment.exception.StorageException;
import com.lms.assessment.model.AssessmentMaterial;
import com.lms.assessment.service.StorageService;
import com.lms.assessment.validation.UploadValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;

/**
 * MVC Controller handling all /assessments endpoints.
 *
 * <ul>
 *   <li>GET  /assessments                   — display upload form + materials list</li>
 *   <li>POST /assessments/upload            — handle file upload</li>
 *   <li>GET  /assessments/download/{filename} — serve a file for download</li>
 * </ul>
 *
 * <p>Validation is fully delegated to {@link UploadValidator} to keep this class
 * focused on HTTP request/response orchestration only.
 */
@Slf4j
@Controller
@RequestMapping("/assessments")
public class AssessmentController {

    // ── In-memory store ───────────────────────────────────────────────────────
    /**
     * Thread-safe list — CopyOnWriteArrayList is ideal for workloads with many reads
     * and occasional writes (uploads are infrequent compared to page views).
     */
    private final List<AssessmentMaterial> materials = new CopyOnWriteArrayList<>();

    /** Thread-safe ID counter — guarantees unique IDs without synchronisation blocks. */
    private final AtomicLong idCounter = new AtomicLong(1);

    // ── Constructor-injected dependencies (no @Autowired field injection) ─────
    private final StorageService  storageService;
    private final UploadValidator uploadValidator;

    public AssessmentController(StorageService storageService, UploadValidator uploadValidator) {
        this.storageService  = storageService;
        this.uploadValidator = uploadValidator;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /assessments
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Renders the main page: passes the materials list and allowed-extension hint
     * to the template so the UI can show an informative file-picker label.
     */
    @GetMapping
    public String listMaterials(Model model) {
        model.addAttribute("materials",          materials);
        model.addAttribute("allowedExtensions",  uploadValidator.getAllowedExtensions());
        model.addAttribute("titleMinLength",     AssessmentMaterial.TITLE_MIN_LENGTH);
        model.addAttribute("titleMaxLength",     AssessmentMaterial.TITLE_MAX_LENGTH);
        model.addAttribute("descMinLength",      AssessmentMaterial.DESCRIPTION_MIN_LENGTH);
        model.addAttribute("descMaxLength",      AssessmentMaterial.DESCRIPTION_MAX_LENGTH);
        log.debug("Rendering assessments page — {} material(s) loaded", materials.size());
        return "assessments";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // POST /assessments/upload
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Handles the multipart form submission.
     *
     * <p>Validation strategy — <em>collect-all</em>:
     * Instead of stopping at the first error, {@link UploadValidator#validate} gathers
     * every problem in a single pass so the user sees the complete list at once.
     * This prevents the "fix one error, see the next" frustration loop.
     *
     * <p>If any errors are found the method redirects immediately with the error list
     * without touching the storage layer or the in-memory materials list.
     */
    @PostMapping("/upload")
    public String uploadMaterial(
            @RequestParam(value = "title",       defaultValue = "") String title,
            @RequestParam(value = "description", defaultValue = "") String description,
            @RequestParam(value = "file")                           MultipartFile file,
            RedirectAttributes redirectAttributes) {

        // ── Step 1: validate all inputs in one pass ───────────────────────────
        List<String> errors = uploadValidator.validate(title, description, file);

        if (!errors.isEmpty()) {
            // Join multiple errors with a separator so a single flash attribute
            // can carry all of them; the template splits on "|" to display each one.
            redirectAttributes.addFlashAttribute("errorMessages", errors);
            log.debug("Upload rejected — {} validation error(s): {}", errors.size(), errors);
            return "redirect:/assessments";
        }

        // ── Step 2: persist the file ──────────────────────────────────────────
        try {
            String savedFilename = storageService.store(file);

            // Build immutable record via factory method (compact constructor validates again)
            AssessmentMaterial material = AssessmentMaterial.of(
                    idCounter.getAndIncrement(),
                    title.strip(),
                    description.strip(),
                    savedFilename
            );

            materials.add(material);
            log.info("Material uploaded — id={}, title='{}', originalFile='{}', savedAs='{}'",
                    material.id(), material.title(), file.getOriginalFilename(), savedFilename);

            redirectAttributes.addFlashAttribute("successMessage",
                    "\"" + file.getOriginalFilename() + "\" uploaded successfully!");

        } catch (StorageException ex) {
            log.error("Upload failed for file '{}': {}", file.getOriginalFilename(), ex.getMessage(), ex);
            redirectAttributes.addFlashAttribute("errorMessages",
                    List.of("Upload failed: " + ex.getMessage()));
        } catch (IllegalArgumentException ex) {
            // Thrown by AssessmentMaterial's compact constructor — should not normally
            // reach here because UploadValidator already catches these, but guarded
            // for defence-in-depth.
            log.error("Material construction rejected: {}", ex.getMessage());
            redirectAttributes.addFlashAttribute("errorMessages",
                    List.of("Invalid input: " + ex.getMessage()));
        }

        return "redirect:/assessments";
    }

    // ─────────────────────────────────────────────────────────────────────────
    // GET /assessments/download/{filename}
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Serves the requested file as an attachment download.
     *
     * <p>StorageException is caught locally and converted to a 404 response.
     * It must NOT propagate to {@code @ControllerAdvice}: that handler returns a
     * redirect String, which Spring cannot write into a {@code ResponseEntity<Resource>}
     * context — doing so would throw an {@code IllegalStateException} at runtime.
     */
    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {

        // ── Guard: reject blank filename in the path ──────────────────────────
        if (filename == null || filename.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        try {
            Resource resource = storageService.loadAsResource(filename);

            // Best-effort Content-Length — omitted gracefully if unavailable
            ResponseEntity.BodyBuilder builder = ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"");

            try {
                long length = resource.contentLength();
                if (length > 0) builder = builder.contentLength(length);
            } catch (Exception ignored) {
                // Non-fatal — browser downloads without Content-Length fine
            }

            return builder.body(resource);

        } catch (StorageException ex) {
            log.warn("Download rejected for '{}': {}", filename, ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
