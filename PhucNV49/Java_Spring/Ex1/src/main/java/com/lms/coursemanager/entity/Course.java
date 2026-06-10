package com.lms.coursemanager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course {

    @EmbeddedId
    private CourseId id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "instructor", nullable = false)
    private String instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Lesson> lessons = new ArrayList<>();
}
