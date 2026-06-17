package com.lms.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "lesson")
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
    private Integer duration; // minutes

    @Column(name = "content_type")
    private String contentType; // Video, Lý thuyết, Thực hành

    @Column(name = "status")
    private String status; // Bản nháp, Đang mở, Đã khóa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    @ToString.Exclude
    private Course course;
}
