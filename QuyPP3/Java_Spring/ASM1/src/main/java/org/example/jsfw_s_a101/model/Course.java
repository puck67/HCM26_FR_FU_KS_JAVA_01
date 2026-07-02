package org.example.jsfw_s_a101.model;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class Course {

    @NotEmpty(message = "Title is required")
    @Size(min = 5, message = "Title must be at least 5 characters")
    private String title;

    @NotEmpty(message = "Instructor name is required")
    @Size(min = 2, message = "Instructor name must be at least 2 characters")
    private String instructorName;

    @NotEmpty(message = "Instructor email is required")
    @Email(message = "Must be a valid email address")
    private String instructorEmail;

    @NotEmpty(message = "Description is required")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotNull(message = "Duration hours is required")
    @Min(value = 1, message = "Duration must be at least 1 hour")
    private Integer durationHours;
}
