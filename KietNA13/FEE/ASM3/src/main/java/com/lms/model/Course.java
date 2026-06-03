package com.lms.model;

import java.io.Serial;
import java.io.Serializable;

public class Course implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String title;
    private final String instructorName;
    private final String description;
    private final int durationHours;

    public Course(String title, String instructorName, String description, int durationHours) {
        this.title          = title;
        this.instructorName = instructorName;
        this.description    = description;
        this.durationHours  = durationHours;
    }

    public String getTitle()          { return title; }
    public String getInstructorName() { return instructorName; }
    public String getDescription()    { return description; }
    public int    getDurationHours()  { return durationHours; }

    public void displayInfo() {
        System.out.printf("[Course: %s] by %s (%d hours): %s%n",
                title, instructorName, durationHours, description);
    }

    @Override
    public String toString() {
        return "Course [title=" + title
             + ", instructor=" + instructorName
             + ", duration=" + durationHours + "]";
    }
}
