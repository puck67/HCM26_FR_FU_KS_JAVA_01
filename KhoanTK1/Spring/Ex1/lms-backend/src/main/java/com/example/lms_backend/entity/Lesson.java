package com.example.lms_backend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "lessons")
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lessonId;

    private String lessonName;
    private Integer duration;
    private String contentType;
    private String status;

    @ManyToOne
    @JoinColumns({
        @JoinColumn(name = "course_code", referencedColumnName = "course_code"),
        @JoinColumn(name = "start_date", referencedColumnName = "start_date")
    })
    @com.fasterxml.jackson.annotation.JsonProperty(access = com.fasterxml.jackson.annotation.JsonProperty.Access.WRITE_ONLY)
    private Course course;
}
