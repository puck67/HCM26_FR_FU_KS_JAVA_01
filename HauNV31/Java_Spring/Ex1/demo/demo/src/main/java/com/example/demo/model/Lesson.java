package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    private Course course;

    @Column(name="lesson_name", nullable = false)
    private String lessonName;
    
    @Column(nullable = false)
    private Integer duration;

    @Column(name = "content_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ContentType contentType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private LessonStatus status;

    public Lesson() {}

    public Lesson(String lessonName, Integer duration, ContentType contentType, LessonStatus status) {
        this.lessonName = lessonName;
        this.duration = duration;
        this.contentType = contentType;
        this.status = status;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getLessonName() { return lessonName; }
    public void setLessonName(String lessonName) { this.lessonName = lessonName; }

    public Integer getDuration() { return duration; }
    public void setDuration(Integer duration) { this.duration = duration; }

    public ContentType getContentType() { return contentType; }
    public void setContentType(ContentType contentType) { this.contentType = contentType; }

    public LessonStatus getStatus() { return status; }
    public void setStatus(LessonStatus status) { this.status = status; }

    public String getCourseCode() {
        return course != null && course.getId() != null ? course.getId().getCourseCode() : null;
    }

    public String getFormattedContentType() {
        if (contentType == null) return "";
        switch (contentType) {
            case VIDEO: return "Video";
            case THEORY: return "Lý thuyết";
            case PRACTICE: return "Thực hành";
            default: return contentType.name();
        }
    }

    public String getFormattedStatus() {
        if (status == null) return "";
        switch (status) {
            case DRAFT: return "Bản nháp";
            case OPEN: return "Đang mở";
            case LOCKED: return "Đã khóa";
            default: return status.name();
        }
    }

    public String getDisplayStatus() {
        if (status == null) return "";
        switch (status) {
            case DRAFT: return "Inactive";
            case OPEN: return "Active";
            case LOCKED: return "Inactive";
            default: return status.name();
        }
    }
}
