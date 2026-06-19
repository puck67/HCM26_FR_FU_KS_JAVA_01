package com.example.lms.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @EmbeddedId
    private CourseId id;
    
    private String courseName;
    private String category;
    private String instructor;
}
