package com.lms.model;

import java.io.Serializable;

/**
 * Course entity - implements Serializable để có thể ghi/đọc từ file
 */
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

    /**
     * In thông tin course theo định dạng yêu cầu:
     * [Course: Spring Framework] by John Doe (40 hours): Learn Spring Core, MVC, and Boot.
     */
    public void displayInfo() {
        System.out.println("[Course: " + title + "] by " + instructorName
                + " (" + durationHours + " hours): " + description);
    }

    // Getters
    public String getTitle() { return title; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
    public int getDurationHours() { return durationHours; }
}
