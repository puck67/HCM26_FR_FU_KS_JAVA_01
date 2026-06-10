package com.lms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.lms.enums.ContentType;
import com.lms.enums.LessonStatus;

@Entity
@Table(name = "lessons")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_name", nullable = false)
    private String lessonName;

    @Column(name = "duration")
    private Integer duration; // in minutes

    @Enumerated(EnumType.STRING)
    @Column(name = "content_type")
    private ContentType contentType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private LessonStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    private Course course;
}
