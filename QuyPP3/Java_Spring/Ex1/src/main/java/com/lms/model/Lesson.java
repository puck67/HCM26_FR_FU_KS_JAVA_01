package com.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "lesson_name", nullable = false, length = 255)
    private String lessonName;

    @Column(name = "duration")
    private Integer duration; // in minutes

    @Column(name = "content_type", length = 50)
    private String contentType; // Video, Lý thuyết, Thực hành

    @Column(name = "status", length = 50)
    private String status; // Bản nháp, Đang mở, Đã khóa

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    private Course course;

    public Lesson() {}

    public Lesson(String lessonName, Integer duration, String contentType, String status, Course course) {
        this.lessonName = lessonName;
        this.duration = duration;
        this.contentType = contentType;
        this.status = status;
        this.course = course;
    }

    // Getters & Setters
    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getLessonName() {
        return lessonName;
    }
    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public Integer getDuration() {
        return duration;
    }
    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getContentType() {
        return contentType;
    }
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }

    public Course getCourse() {
        return course;
    }
    public void setCourse(Course course) {
        this.course = course;
    }
}
