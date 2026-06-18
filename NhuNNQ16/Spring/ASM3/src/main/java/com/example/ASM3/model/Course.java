package com.example.ASM3.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course implements Serializable {
    private String title;
    private String instructorName;
    private String description;
    private int durationHours;

    public void displayInfo(){
        System.out.printf("[Course: %s] by %s (%d hours): %s%n",
                title, instructorName, durationHours, description);
    }
    @Override
    public String toString() {
        return "Course [title=" + title + ", instructor=" + instructorName + ", duration=" + durationHours + "]";
    }
}
