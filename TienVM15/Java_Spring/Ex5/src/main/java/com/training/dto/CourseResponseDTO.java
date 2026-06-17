package com.training.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseResponseDTO {
    private Long id;
    private String courseName;
    private Integer duration;
    private String description;
}
