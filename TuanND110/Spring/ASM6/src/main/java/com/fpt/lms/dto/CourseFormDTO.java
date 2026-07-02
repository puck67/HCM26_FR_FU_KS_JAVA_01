package com.fpt.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * DTO for Course form submission.
 * Decouples form binding from JPA entity — prevents mass-assignment vulnerabilities.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseFormDTO {

    private Long id;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;

    @NotBlank(message = "Content is required")
    private String content;

    /** Comma-separated category names, e.g. "Java,Spring,Backend" */
    private String category;

    /** 1=DRAFT, 2=PUBLISHED, 3=ARCHIVED — defaults to DRAFT */
    private Integer status;
}
