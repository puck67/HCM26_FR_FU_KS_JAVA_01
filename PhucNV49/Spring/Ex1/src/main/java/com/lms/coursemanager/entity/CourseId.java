package com.lms.coursemanager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseId implements Serializable {

    @Column(name = "course_code", nullable = false)
    private String courseCode;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CourseId courseId = (CourseId) o;
        return Objects.equals(courseCode, courseId.courseCode) && Objects.equals(startDate, courseId.startDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode, startDate);
    }
}
