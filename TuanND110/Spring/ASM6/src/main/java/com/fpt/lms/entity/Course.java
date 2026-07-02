package com.fpt.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Course entity — represents a learning course in the LMS.
 * Uses Lombok to eliminate boilerplate getters/setters.
 * Status is managed via {@link CourseStatus} enum.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is required")
    @Column(nullable = false)
    private String title;

    @NotBlank(message = "Description is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @NotBlank(message = "Content is required")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /**
     * Integer backing field for status, stored in DB.
     * Use {@link #getStatusEnum()} for type-safe access.
     * 1=DRAFT, 2=PUBLISHED, 3=ARCHIVED
     */
    @Builder.Default
    @Column(nullable = false)
    private Integer status = CourseStatus.DRAFT.getValue();

    /** Comma-separated category names, e.g. "Java,Spring,Backend" */
    private String category;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Instructor instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (status == null) {
            status = CourseStatus.DRAFT.getValue();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /** Convenience method to get type-safe status enum */
    public CourseStatus getStatusEnum() {
        return CourseStatus.fromValue(this.status);
    }

    /** Convenience method to set status via enum */
    public void setStatusEnum(CourseStatus courseStatus) {
        this.status = courseStatus.getValue();
    }
}
