package com.example.lms_backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CourseId implements Serializable {

    @Column(name = "course_code")
    private String courseCode;

    @Column(name = "start_date")
    private LocalDate startDate;
}
