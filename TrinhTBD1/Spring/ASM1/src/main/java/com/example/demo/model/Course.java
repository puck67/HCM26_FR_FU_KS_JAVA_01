package com.example.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Course title must not be empty")
    @Size(min = 5, message = "Course title must have at least 5 characters")
    private String title;

    @NotEmpty(message = "Instructor name must not be empty")
    @Size(min = 2, message = "Instructor name must have at least 2 characters")
    private String instructorName;

    @NotEmpty(message = "Instructor email must not be empty")
    @Email(message = "Instructor email must be a valid email address")
    private String instructorEmail;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotNull(message = "Duration must not be null")
    @Min(value = 1, message = "Duration must be at least 1")
    private Integer durationHours;
}
