package com.example.asm2.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AssessmentMaterial {
    private long id;
    private String title;
    private String description;
    private String fileName;
}
