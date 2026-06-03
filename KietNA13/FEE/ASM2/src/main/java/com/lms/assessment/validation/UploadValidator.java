package com.lms.assessment.validation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Centralised validation component for upload form inputs.
 *
 * <p>Separating validation into its own class (Single Responsibility Principle) keeps
 * the controller thin and makes it easy to add or change rules without touching
 * request-handling logic.
 *
 * <p>All limits are externalised to {@code application.properties} so they can be
 * changed without recompiling the application.
 */
@Slf4j
@Component
public class UploadValidator {

    // ── Injected constraints ──────────────────────────────────────────────────
    private final int    titleMinLength;
    private final int    titleMaxLength;
    private final int    descMinLength;
    private final int    descMaxLength;

    /**
     * Set of allowed lowercase extensions, built once from the comma-separated
     * property value. Stored as a Set for O(1) lookup at validation time.
     */
    private final Set<String> allowedExtensions;

    public UploadValidator(
            @Value("${validation.title.min-length:3}")         int titleMinLength,
            @Value("${validation.title.max-length:200}")        int titleMaxLength,
            @Value("${validation.description.min-length:10}")   int descMinLength,
            @Value("${validation.description.max-length:1000}") int descMaxLength,
            @Value("${storage.allowed-extensions:pdf,doc,docx,xls,xlsx,ppt,pptx,txt,zip,png,jpg,jpeg,gif,mp4,csv}")
                    String allowedExtensionsCsv) {

        this.titleMinLength = titleMinLength;
        this.titleMaxLength = titleMaxLength;
        this.descMinLength  = descMinLength;
        this.descMaxLength  = descMaxLength;

        // Split CSV, strip whitespace, lowercase — build an immutable lookup Set
        this.allowedExtensions = Arrays.stream(allowedExtensionsCsv.split(","))
                .map(String::strip)
                .map(String::toLowerCase)
                .collect(Collectors.toUnmodifiableSet());

        log.debug("UploadValidator initialised — allowed extensions: {}", allowedExtensions);
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Public API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Validates all three upload fields at once and returns a list of human-readable
     * error messages.  An empty list means the input is fully valid.
     *
     * <p>Collecting ALL errors in one pass (instead of returning on first failure)
     * gives the user a complete picture of what needs fixing.
     *
     * @param title       the raw title string from the form
     * @param description the raw description string from the form
     * @param file        the uploaded multipart file
     * @return list of error messages; empty if all inputs are valid
     */
    public List<String> validate(String title, String description, MultipartFile file) {
        List<String> errors = new ArrayList<>();
        validateTitle(title, errors);
        validateDescription(description, errors);
        validateFile(file, errors);
        return errors;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers — each field has its own method for clarity
    // ─────────────────────────────────────────────────────────────────────────

    private void validateTitle(String title, List<String> errors) {
        if (title == null) {
            errors.add("Title is required.");
            return;
        }
        String t = title.strip();
        if (t.isEmpty()) {
            errors.add("Title must not be blank.");
            return; // length checks don't make sense for blank input
        }
        if (t.length() < titleMinLength) {
            errors.add(String.format(
                    "Title is too short: minimum %d characters (you entered %d).",
                    titleMinLength, t.length()));
        }
        if (t.length() > titleMaxLength) {
            errors.add(String.format(
                    "Title is too long: maximum %d characters (you entered %d).",
                    titleMaxLength, t.length()));
        }
    }

    private void validateDescription(String description, List<String> errors) {
        if (description == null) {
            errors.add("Description is required.");
            return;
        }
        String d = description.strip();
        if (d.isEmpty()) {
            errors.add("Description must not be blank.");
            return;
        }
        if (d.length() < descMinLength) {
            errors.add(String.format(
                    "Description is too short: minimum %d characters (you entered %d).",
                    descMinLength, d.length()));
        }
        if (d.length() > descMaxLength) {
            errors.add(String.format(
                    "Description is too long: maximum %d characters (you entered %d).",
                    descMaxLength, d.length()));
        }
    }

    private void validateFile(MultipartFile file, List<String> errors) {
        if (file == null || file.isEmpty()) {
            errors.add("Please select a file to upload.");
            return; // no point checking extension of a missing file
        }

        String originalName = file.getOriginalFilename();
        if (originalName == null || originalName.isBlank()) {
            errors.add("The uploaded file has no filename.");
            return;
        }

        // Extract extension (everything after the last dot, lowercased)
        int dotIndex = originalName.lastIndexOf('.');
        if (dotIndex < 0 || dotIndex == originalName.length() - 1) {
            errors.add("File has no extension. Allowed types: " + formattedExtensions());
            return;
        }

        String ext = originalName.substring(dotIndex + 1).toLowerCase();
        if (!allowedExtensions.contains(ext)) {
            errors.add(String.format(
                    "File type '.%s' is not allowed. Allowed types: %s",
                    ext, formattedExtensions()));
        }
    }

    /**
     * Returns a sorted, comma-separated display string of allowed extensions for
     * use in error messages (e.g., "csv, doc, docx, gif, jpg, …").
     */
    private String formattedExtensions() {
        return allowedExtensions.stream()
                .sorted()
                .collect(Collectors.joining(", "));
    }

    /** Exposes the allowed extension set for informational display in templates. */
    public Set<String> getAllowedExtensions() {
        return allowedExtensions;
    }
}
