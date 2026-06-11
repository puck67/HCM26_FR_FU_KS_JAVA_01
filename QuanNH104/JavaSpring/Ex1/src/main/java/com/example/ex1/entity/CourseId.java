package com.example.ex1.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CourseId implements Serializable {

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "start_date")
    @org.springframework.format.annotation.DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    // Constructor không tham số
    public CourseId() {
    }

    // Constructor nhận đầy đủ tham số
    public CourseId(String courseCode, LocalDate startDate) {
        this.courseCode = courseCode;
        this.startDate = startDate;
    }

    // Getters and Setters
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseId courseId = (CourseId) o;
        return Objects.equals(courseCode, courseId.courseCode) &&
                Objects.equals(startDate, courseId.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, startDate);
    }
}
