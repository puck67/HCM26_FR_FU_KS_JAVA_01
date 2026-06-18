package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;

@Entity
@Table(name = "course")
@IdClass(CourseId.class)
public class Course {

    @Id
    @Column(name = "course_code")
    @NotBlank(message = "Course Code cannot be blank")
    @Pattern(regexp = "^[A-Za-z0-9\\-]+$", message = "Course Code must be alphanumeric (optionally with hyphens)")
    private String courseCode;

    @Id
    @Column(name = "start_date")
    @NotNull(message = "Start Date cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @Column(name = "course_name")
    @NotBlank(message = "Course Name cannot be blank")
    private String courseName;

    @NotBlank(message = "Category cannot be blank")
    private String category;

    @NotBlank(message = "Instructor cannot be blank")
    private String instructor;

    public Course() {}

    public Course(String courseCode, LocalDate startDate, String courseName, String category, String instructor) {
        this.courseCode = courseCode;
        this.startDate = startDate;
        this.courseName = courseName;
        this.category = category;
        this.instructor = instructor;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }
}
