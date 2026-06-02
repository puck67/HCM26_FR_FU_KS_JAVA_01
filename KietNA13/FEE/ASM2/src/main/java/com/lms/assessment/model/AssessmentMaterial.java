package com.lms.assessment.model;

import java.util.Objects;

/**
 * Immutable record holding metadata about an uploaded assessment material.
 * The compact constructor enforces ALL invariants at creation time (fail-fast),
 * so every AssessmentMaterial instance in memory is guaranteed to be valid.
 */
public record AssessmentMaterial(
        long   id,
        String title,
        String description,
        String fileName
) {
    // ── Constants (mirrors application.properties validation.* values) ────────
    public static final int TITLE_MIN_LENGTH       = 3;
    public static final int TITLE_MAX_LENGTH       = 200;
    public static final int DESCRIPTION_MIN_LENGTH = 10;
    public static final int DESCRIPTION_MAX_LENGTH = 1000;

    /**
     * Compact constructor — runs automatically before fields are assigned.
     * All invariants are checked here so no invalid object can ever be created.
     */
    public AssessmentMaterial {
        // ── id ────────────────────────────────────────────────────────────────
        if (id <= 0) {
            throw new IllegalArgumentException("id must be a positive number, got: " + id);
        }

        // ── title ─────────────────────────────────────────────────────────────
        Objects.requireNonNull(title, "title must not be null");
        String strippedTitle = title.strip();
        if (strippedTitle.isEmpty()) {
            throw new IllegalArgumentException("title must not be blank");
        }
        if (strippedTitle.length() < TITLE_MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "title must be at least " + TITLE_MIN_LENGTH + " characters");
        }
        if (strippedTitle.length() > TITLE_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "title must be at most " + TITLE_MAX_LENGTH + " characters");
        }
        // Compact constructor can reassign to store the stripped value
        title = strippedTitle;

        // ── description ───────────────────────────────────────────────────────
        Objects.requireNonNull(description, "description must not be null");
        String strippedDesc = description.strip();
        if (strippedDesc.isEmpty()) {
            throw new IllegalArgumentException("description must not be blank");
        }
        if (strippedDesc.length() < DESCRIPTION_MIN_LENGTH) {
            throw new IllegalArgumentException(
                    "description must be at least " + DESCRIPTION_MIN_LENGTH + " characters");
        }
        if (strippedDesc.length() > DESCRIPTION_MAX_LENGTH) {
            throw new IllegalArgumentException(
                    "description must be at most " + DESCRIPTION_MAX_LENGTH + " characters");
        }
        description = strippedDesc;

        // ── fileName ──────────────────────────────────────────────────────────
        Objects.requireNonNull(fileName, "fileName must not be null");
        if (fileName.isBlank()) {
            throw new IllegalArgumentException("fileName must not be blank");
        }
    }

    /**
     * Static factory method for readable construction at call sites.
     */
    public static AssessmentMaterial of(long id, String title, String description, String fileName) {
        return new AssessmentMaterial(id, title, description, fileName);
    }
}
