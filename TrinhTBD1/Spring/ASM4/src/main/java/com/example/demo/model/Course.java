package com.example.demo.model;

import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter
@Setter
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
        StringBuilder builder = new StringBuilder();
        builder.append("[Course: ")
               .append(this.title != null ? this.title.trim() : "Unknown")
               .append("] by ")
               .append(this.instructorName != null ? this.instructorName.trim() : "Unknown")
               .append(" (")
               .append(this.durationHours)
               .append(" hours): ")
               .append(this.description != null ? this.description.trim() : "");
        System.out.println(builder.toString());
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Course [title=")
               .append(this.title)
               .append(", instructor=")
               .append(this.instructorName)
               .append(", duration=")
               .append(this.durationHours)
               .append("]");
        return builder.toString();
    }
}
