package com.fpt.lms.model;

import java.io.Serializable;

public class Course implements Serializable {
    private static final long serialVersionUID = 1L;

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
        System.out.println("[Course: " + title + "] by " + instructorName +
                " (" + durationHours + " hours): " + description);
    }

    @Override
    public String toString() {
        return "Course [title=" + title + ", instructor=" + instructorName +
                ", duration=" + durationHours + "]";
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getInstructorName() { return instructorName; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public int getDurationHours() { return durationHours; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }
}
