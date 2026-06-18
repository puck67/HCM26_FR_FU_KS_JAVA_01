package com.lms.materialmanager.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "materials")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "material_id")
    private Long materialId;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "stored_file_name", nullable = false)
    private String storedFileName;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "file_type")
    private String fileType;

    @Column(name = "upload_date", nullable = false)
    private LocalDateTime uploadDate = LocalDateTime.now();

    @NotBlank(message = "Description is required")
    @Column(nullable = false)
    private String description;

    // Bonus 3: Material Categories (Lecture, Assignment, Reference, Source Code)
    @Column(nullable = false)
    private String category;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public String getFormattedSize() {
        if (fileSize == null || fileSize <= 0) return "0 B";
        final String[] units = new String[] { "B", "KB", "MB", "GB", "TB" };
        int digitGroups = 0;
        double size = fileSize;
        while (size >= 1024 && digitGroups < units.length - 1) {
            size /= 1024;
            digitGroups++;
        }
        return String.format("%.1f %s", size, units[digitGroups]);
    }

    public String getIconClass() {
        if (fileName == null) return "fa-solid fa-file text-muted";
        String fn = fileName.toLowerCase();
        if (fn.endsWith(".pdf")) return "fa-solid fa-file-pdf text-danger";
        if (fn.endsWith(".docx")) return "fa-solid fa-file-word text-primary";
        if (fn.endsWith(".pptx")) return "fa-solid fa-file-powerpoint text-warning";
        if (fn.endsWith(".zip")) return "fa-solid fa-file-zipper text-info";
        if (fn.endsWith(".txt")) return "fa-solid fa-file-lines text-secondary";
        return "fa-solid fa-file text-muted";
    }
}
