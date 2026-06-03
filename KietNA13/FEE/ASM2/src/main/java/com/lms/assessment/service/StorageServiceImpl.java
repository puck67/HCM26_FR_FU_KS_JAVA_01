package com.lms.assessment.service;

import com.lms.assessment.exception.StorageException;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Concrete implementation of {@link StorageService} using Java NIO2.
 *
 * <p>All file I/O uses {@link Files} and {@link Path} — no legacy java.io.File.
 *
 * <p>Validation is applied here as a <b>second layer of defence</b>: even if the
 * controller skips its own checks (e.g. direct API call), the service will still
 * reject invalid input before any disk operation occurs.
 */
@Slf4j
@Service
public class StorageServiceImpl implements StorageService {

    /** Resolved, absolute upload directory path. */
    private final Path uploadDir;

    /**
     * Set of allowed lowercase extensions built from the CSV property.
     * Stored as Set for O(1) lookup.
     */
    private final Set<String> allowedExtensions;

    /**
     * Constructor injection — all dependencies come from application.properties.
     */
    public StorageServiceImpl(
            @Value("${storage.upload-dir:./uploads}") String uploadDirStr,
            @Value("${storage.allowed-extensions:pdf,doc,docx,xls,xlsx,ppt,pptx,txt,zip,png,jpg,jpeg,gif,mp4,csv}")
                    String allowedExtensionsCsv) {

        this.uploadDir = Paths.get(uploadDirStr).toAbsolutePath().normalize();

        this.allowedExtensions = Arrays.stream(allowedExtensionsCsv.split(","))
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());

        log.info("StorageServiceImpl configured — uploadDir='{}', allowedExtensions={}",
                uploadDir, allowedExtensions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // StorageService implementation
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Creates the upload directory on application startup.
     * {@link Files#createDirectories} is idempotent — safe to call even if dir exists.
     */
    @PostConstruct
    @Override
    public void init() {
        try {
            Files.createDirectories(uploadDir);
            log.info("Storage directory initialised at: {}", uploadDir);
        } catch (IOException ex) {
            throw new StorageException("Could not initialise upload directory: " + uploadDir, ex);
        }
    }

    /**
     * Validates, sanitises, and persists the uploaded file.
     *
     * <p>Validation order (fail-fast — first bad condition throws):
     * <ol>
     *   <li>File must not be empty/null</li>
     *   <li>Filename must be present after sanitisation</li>
     *   <li>Filename must not contain path-traversal sequences ({@code ..})</li>
     *   <li>Extension must be in the configured whitelist</li>
     * </ol>
     *
     * <p>On successful validation the file is written with a UUID prefix to prevent
     * collisions.  If the write fails mid-stream, the partial file is deleted to
     * avoid leaving corrupt data on disk.
     *
     * @param file the uploaded multipart file
     * @return the unique server-side filename (UUID prefix + original name)
     * @throws StorageException on any validation or I/O failure
     */
    @Override
    public String store(MultipartFile file) throws StorageException {

        // ── Guard 1: reject null / empty file ─────────────────────────────────
        if (file == null || file.isEmpty()) {
            throw new StorageException("Cannot store an empty or null file.");
        }

        // ── Guard 2: require a valid filename ─────────────────────────────────
        // StringUtils.cleanPath() strips path-traversal sequences (e.g. "../")
        String originalFilename = Optional.ofNullable(file.getOriginalFilename())
                .map(StringUtils::cleanPath)
                .map(String::strip)
                .filter(name -> !name.isBlank())
                .orElseThrow(() -> new StorageException("Uploaded file has no valid filename."));

        // ── Guard 3: block any remaining path-traversal after sanitisation ────
        if (originalFilename.contains("..")) {
            throw new StorageException(
                    "Filename contains an invalid path sequence: '" + originalFilename + "'.");
        }

        // ── Guard 4: extension whitelist ──────────────────────────────────────
        validateExtension(originalFilename);

        // ── Persist with UUID prefix ──────────────────────────────────────────
        // UUID prefix guarantees uniqueness even when two users upload "assignment.pdf"
        String uniqueFilename = UUID.randomUUID() + "_" + originalFilename;
        Path   targetPath     = uploadDir.resolve(uniqueFilename);

        try {
            Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
            log.info("File stored — uniqueName='{}', size={} bytes, originalName='{}'",
                    uniqueFilename, file.getSize(), originalFilename);
            return uniqueFilename;

        } catch (IOException ex) {
            // Cleanup: remove partially-written file to avoid corrupt data on disk
            silentDelete(targetPath);
            throw new StorageException(
                    "I/O error while storing file '" + originalFilename + "'. Please try again.", ex);
        }
    }

    /**
     * Loads a stored file as a Spring {@link Resource} and performs security/existence checks.
     *
     * <p>Checks (in order):
     * <ol>
     *   <li>Filename must not be null or blank</li>
     *   <li>After resolving and normalising, the path must still be inside {@code uploadDir}
     *       (prevents path-traversal attacks that survive {@code cleanPath()})</li>
     *   <li>File must physically exist and be readable</li>
     * </ol>
     *
     * @param filename the server-side filename to serve
     * @return the file wrapped as a readable {@link Resource}
     * @throws StorageException if any check fails
     */
    @Override
    public Resource loadAsResource(String filename) throws StorageException {

        // ── Guard 1: reject blank filename ────────────────────────────────────
        if (filename == null || filename.isBlank()) {
            throw new StorageException("Filename must not be blank.");
        }

        try {
            Path filePath = uploadDir.resolve(StringUtils.cleanPath(filename)).normalize();

            // ── Guard 2: path-confinement check ───────────────────────────────
            // Even after cleanPath() + normalize(), a crafted input like
            // "%2F..%2F..%2Fetc%2Fpasswd" (URL-decoded) could escape the directory.
            // Comparing path prefixes is the only reliable safeguard.
            if (!filePath.startsWith(uploadDir)) {
                log.warn("Path-traversal attempt blocked — filename='{}', resolved='{}'",
                        filename, filePath);
                throw new StorageException(
                        "Access denied: filename escapes the upload directory.");
            }

            Resource resource = new UrlResource(filePath.toUri());

            // ── Guard 3: existence + readability ──────────────────────────────
            // Use NIO2 Files.exists() for an explicit filesystem check
            if (!Files.exists(filePath)) {
                throw new StorageException("File not found: '" + filename + "'.");
            }
            if (!resource.isReadable()) {
                throw new StorageException("File exists but is not readable: '" + filename + "'.");
            }

            log.debug("Serving resource: '{}'", filePath);
            return resource;

        } catch (MalformedURLException ex) {
            throw new StorageException("Malformed file path for filename: '" + filename + "'.", ex);
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Extracts the file extension from {@code filename} and verifies it is in
     * {@link #allowedExtensions}.  The check is case-insensitive.
     *
     * @throws StorageException if the extension is missing or not whitelisted
     */
    private void validateExtension(String filename) {
        int dotIndex = filename.lastIndexOf('.');

        if (dotIndex < 0 || dotIndex == filename.length() - 1) {
            throw new StorageException(
                    "File '" + filename + "' has no extension. "
                    + "Allowed types: " + sortedExtensions());
        }

        String ext = filename.substring(dotIndex + 1).toLowerCase();

        if (!allowedExtensions.contains(ext)) {
            throw new StorageException(String.format(
                    "File type '.%s' is not permitted. Allowed types: %s",
                    ext, sortedExtensions()));
        }
    }

    /** Tries to delete {@code path}, logging a warning if deletion fails (non-fatal). */
    private void silentDelete(Path path) {
        try {
            Files.deleteIfExists(path);
            log.warn("Partial file deleted after failed write: '{}'", path);
        } catch (IOException ex) {
            log.error("Could not delete partial file '{}': {}", path, ex.getMessage());
        }
    }

    /** Returns a sorted, readable list of allowed extensions for error messages. */
    private String sortedExtensions() {
        return allowedExtensions.stream()
                .sorted()
                .collect(Collectors.joining(", "));
    }
}
