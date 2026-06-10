package com.lms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

@Entity
@Table(name = "lesson")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lesson_name", nullable = false)
    @NotBlank(message = "Lesson Name cannot be blank")
    private String lessonName;

    @NotNull(message = "Duration cannot be null")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @Column(name = "content_type")
    @NotBlank(message = "Content Type cannot be blank")
    @Pattern(regexp = "^(Video|Theory|Practice)$", message = "Content Type must be Video, Theory, or Practice")
    private String contentType;

    @NotBlank(message = "Status cannot be blank")
    @Pattern(regexp = "^(Draft|Open|Locked)$", message = "Status must be Draft, Open, or Locked")
    private String status;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    @NotNull(message = "Course reference cannot be null")
    private Course course;

    public Lesson() {}

    public Lesson(String lessonName, Integer duration, String contentType, String status, Course course) {
        this.lessonName = lessonName;
        this.duration = duration;
        this.contentType = contentType;
        this.status = status;
        this.course = course;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
