package com.example.demo.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentMaterial {

    private long id;

    @NotBlank(message = "Title is required and cannot be blank.")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters.")
    @Pattern(regexp = "^[a-zA-Z0-9\\s\\-_.]+$", message = "Title can only contain letters, numbers, spaces, hyphens, underscores, and dots.")
    private String title;

    @NotBlank(message = "Description is required and cannot be blank.")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters.")
    @Pattern(regexp = "^[^<>]*$", message = "Description cannot contain HTML tags or '<' / '>' symbols.")
    private String description;

    private String fileName;
}
