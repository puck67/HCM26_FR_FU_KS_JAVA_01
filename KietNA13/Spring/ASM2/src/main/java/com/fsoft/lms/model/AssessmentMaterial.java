package com.fsoft.lms.model;

/**
 * Model lưu metadata của một tài liệu assessment đã được upload.
 * Không serialize vào DB — chỉ lưu in-memory trong phiên chạy ứng dụng.
 */
public class AssessmentMaterial {

    private final long id;
    private final String title;
    private final String description;
    private final String fileName;

    public AssessmentMaterial(long id, String title, String description, String fileName) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.fileName = fileName;
    }

    // ── Getters (immutable — không cần setter sau khi tạo) ─────────────────────

    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getFileName() { return fileName; }
}
