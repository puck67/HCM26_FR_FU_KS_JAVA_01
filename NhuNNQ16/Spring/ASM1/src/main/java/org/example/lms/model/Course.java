package org.example.lms.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class Course {

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

    @NotNull(message = "Duration hours must not be null")
    @Min(value = 1, message = "Duration hours must be at least 1")
    private Integer durationHours;


    public Course() {
    }

    public Course(String title, String instructorName, String instructorEmail, String description, Integer durationHours) {
        this.title = title;
        this.instructorName = instructorName;
        this.instructorEmail = instructorEmail;
        this.description = description;
        this.durationHours = durationHours;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getInstructorEmail() {
        return instructorEmail;
    }

    public void setInstructorEmail(String instructorEmail) {
        this.instructorEmail = instructorEmail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(Integer durationHours) {
        this.durationHours = durationHours;
    }
}
