package com.example.ASM4.model;

import java.io.Serializable;

public class Course implements Serializable {
    private String title;
    private String instructorName;
    private String description;
    private int durationHours;

    public Course() {
    }

    public Course(String title, String instructorName, String description, int durationHours) {
        this.title = title;
        this.instructorName = instructorName;
        this.description = description;
        this.durationHours = durationHours;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDurationHours() {
        return durationHours;
    }

    public void setDurationHours(int durationHours) {
        this.durationHours = durationHours;
    }

    public void displayInfo(){
        System.out.printf("[Course: %s] by %s (%d hours): %s%n",
                title, instructorName, durationHours, description);
    }
}
