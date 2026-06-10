package com.lms.coursemanager.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonDTO {

    private Long id;

    @NotBlank(message = "Lesson name is required")
    private String lessonName;

    @NotNull(message = "Duration is required")
    @Min(value = 1, message = "Duration must be at least 1 minute")
    private Integer duration;

    @NotBlank(message = "Content type is required")
    private String contentType;

    @NotBlank(message = "Status is required")
    private String status;
}
