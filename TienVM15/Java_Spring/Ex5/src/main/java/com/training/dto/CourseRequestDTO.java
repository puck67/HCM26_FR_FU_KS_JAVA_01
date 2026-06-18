package com.training.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CourseRequestDTO {

    @NotBlank(message = "Course name is required")
    private String courseName;

    @Min(value = 1, message = "Duration must be at least 1 hour")
    private Integer duration;

    private String description;
}
