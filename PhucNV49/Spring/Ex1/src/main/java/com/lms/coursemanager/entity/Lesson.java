package com.lms.coursemanager.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "status", nullable = false)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    @ToString.Exclude
    private Course course;
}
