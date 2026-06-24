package com.fpt.lms.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Course {

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
