package com.fpt.lms.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * Review entity — represents a student review on a Course.
 * Status: 1=PENDING, 2=APPROVED.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "tbl_review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String authorName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email")
    @Column(nullable = false)
    private String email;

    @NotNull(message = "Rating is required")
    @Min(value = 1, message = "Rating must be at least 1")
    @Max(value = 5, message = "Rating must be at most 5")
    @Column(nullable = false)
    private Integer rating;

    @NotBlank(message = "Review content is required")
    @Size(min = 10, max = 2000, message = "Review must be between 10 and 2000 characters")
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    /** 1=PENDING, 2=APPROVED */
    @Builder.Default
    @Column(nullable = false)
    private Integer status = 1;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        if (status == null) {
            status = 1;
        }
    }
}
