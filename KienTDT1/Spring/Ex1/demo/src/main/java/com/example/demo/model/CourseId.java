package com.example.demo.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
public class CourseId implements Serializable {
    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    public CourseId() {
    }

    public CourseId(String courseCode, LocalDate startDate) {
        this.courseCode = courseCode;
        this.startDate = startDate;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseId courseId)) {
            return false;
        }
        return Objects.equals(courseCode, courseId.courseCode)
                && Objects.equals(startDate, courseId.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, startDate);
    }
}
