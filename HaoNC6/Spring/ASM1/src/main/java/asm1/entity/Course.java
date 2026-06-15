package asm1.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty(message = "Title must not be empty")
    @Size(min = 5, message = "Title must have at least 5 characters")
    private String title;

    @NotEmpty(message = "Instructor name must not be empty")
    @Size(min = 2, message = "Instructor name must have at least 2 characters")
    private String instructorName;


    @NotEmpty(message = "Instructor email must not be empty")
    @Email(message = "Instructor email must be a valid email address")
    private String instructorEmail;

    @NotEmpty(message = "Description must not be empty")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters long")
    private String description;

    @NotNull(message = "Duration must not be null")
    @Min(value = 1, message = "Duration hours must be at least 1")
    private Integer durationHours;
}
