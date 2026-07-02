package com.fpt.lms.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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

    @NotEmpty
    @Size(min = 5, message = "Title must have at least 5 characters")
    private String title;

    @NotEmpty
    @Size(min = 2, message = "Instructor name must have at least 2 characters")
    private String instructorName;

    @NotEmpty
    @Email(message = "Must be a valid email address")
    private String instructorEmail;

    @NotEmpty
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotNull
    @Min(value = 1, message = "Duration must be at least 1 hour")
    private Integer durationHours;
}
