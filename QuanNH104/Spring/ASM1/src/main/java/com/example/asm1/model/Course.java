package com.example.asm1.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @NotBlank(message = "Course title must not be empty")
    @Size(min = 5, message = "Course title must have at least 5 characters")
    private String title;

    @NotBlank(message = "Instructor name must not be empty")
    @Size(min = 2, message = "Instructor name must have at least 2 characters")
    private String instructorName;

    @NotBlank(message = "Instructor email must not be empty")
    @Email(message = "Please provide a valid email address")
    private String instructorEmail;

    @NotBlank(message = "Description must not be empty")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotNull(message = "Duration hours must not be null")
    @Min(value = 1, message = "Duration hours must be at least 1")
    private Integer durationHours;
}
