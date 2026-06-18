package com.example.ASM2.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssessmentMaterial {
private long id;
private String title;
private String description;
private String fileName;
}
