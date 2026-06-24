package com.example.asm3.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

    private String title;
    private String instructorName;
    private String description;
    private int durationHours;

    // Function to display course information
    public void displayInfo() {
        System.out.println("[Course: " + title + "] by " + instructorName + " (" + durationHours + " hours): " + description);
    }

    // Overridden toString method as specified by the requirements
    @Override
    public String toString() {
        return "Course [title=" + title + ", instructor=" + instructorName + ", duration=" + durationHours + "]";
    }
}
