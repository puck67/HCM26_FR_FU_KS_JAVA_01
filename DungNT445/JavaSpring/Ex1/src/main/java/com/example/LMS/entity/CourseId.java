package com.example.LMS.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class CourseId implements Serializable {

    private String courseCode;
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
