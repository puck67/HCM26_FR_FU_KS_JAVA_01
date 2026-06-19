package com.example.menu.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Lesson {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumns({
        @JoinColumn(name = "courseCode", referencedColumnName = "courseCode"),
        @JoinColumn(name = "startDate", referencedColumnName = "startDate")
    })
    private Course course;

    private String lessonName;
    private Integer duration;
    private String contentType;
    private String status;
}
