package com.example.training.dto;

import lombok.Data;

@Data
public class CourseResponseDTO {
    private Long id;
    private String courseName;
    private String duration;
    private String instructor;
}
