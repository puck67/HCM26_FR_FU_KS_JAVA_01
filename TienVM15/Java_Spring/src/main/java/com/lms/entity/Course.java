package com.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@IdClass(CourseId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @Column(name = "course_code")
    @jakarta.validation.constraints.NotBlank(message = "Course code is required")
    private String courseCode;

    @Id
    @Column(name = "start_date")
    @jakarta.validation.constraints.NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Column(name = "course_name", nullable = false)
    @jakarta.validation.constraints.NotBlank(message = "Course name is required")
    private String courseName;

    @Column(name = "category")
    private String category;

    @Column(name = "instructor")
    @jakarta.validation.constraints.NotBlank(message = "Instructor is required")
    private String instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();
}
