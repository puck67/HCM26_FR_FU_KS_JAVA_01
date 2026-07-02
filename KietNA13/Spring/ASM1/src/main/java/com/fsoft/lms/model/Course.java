package com.fsoft.lms.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Model đại diện cho một Course trong hệ thống LMS.
 * Dùng Bean Validation để đảm bảo dữ liệu hợp lệ trước khi xử lý.
 */
public class Course {

    @NotBlank(message = "Title must not be blank")
    @Size(min = 5, max = 100, message = "Title must be between 5 and 100 characters")
    private String title;

    @NotBlank(message = "Instructor name must not be blank")
    @Size(min = 2, max = 50, message = "Instructor name must be between 2 and 50 characters")
    private String instructorName;

    @NotBlank(message = "Instructor email must not be blank")
    @Email(message = "Instructor email must be a valid email address")
    private String instructorEmail;

    @NotBlank(message = "Description must not be blank")
    @Size(min = 10, max = 200, message = "Description must be between 10 and 200 characters")
    private String description;

    @NotNull(message = "Duration hours must not be null")
    @Min(value = 1, message = "Duration hours must be at least 1")
    @Max(value = 1000, message = "Duration hours must not exceed 1000")
    private Integer durationHours;

    public Course() {}

    // ── Getters & Setters ──────────────────────────────────────────────────────

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }

    public String getInstructorEmail() { return instructorEmail; }
    public void setInstructorEmail(String instructorEmail) { this.instructorEmail = instructorEmail; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Integer getDurationHours() { return durationHours; }
    public void setDurationHours(Integer durationHours) { this.durationHours = durationHours; }
}
