package org.example.jsfw_m_a101.model;

import java.io.Serializable;

public class Course implements Serializable {

    private String title;
    private String instructorName;
    private String description;
    private int durationHours;

    public Course(String title, String instructorName, String description, int durationHours) {
        this.title = title;
        this.instructorName = instructorName;
        this.description = description;
        this.durationHours = durationHours;
    }

    public void displayInfo() {
        System.out.println("[Course: " + title + "] by " + instructorName
                + " (" + durationHours + " hours): " + description);
    }

    @Override
    public String toString() {
        return "Course [title=" + title + ", instructor=" + instructorName + ", duration=" + durationHours + "]";
    }

    // Getters
    public String getTitle() { return title; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
    public int getDurationHours() { return durationHours; }
}
