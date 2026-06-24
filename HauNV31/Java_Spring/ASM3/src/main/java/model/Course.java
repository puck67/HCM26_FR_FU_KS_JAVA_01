package model;

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
        System.out.println(new StringBuilder("Course: [")
                .append("title=").append(title)
                .append(", instructor=").append(instructorName)
                .append(", description=").append(description)
                .append(", duration=").append(durationHours).append("h")
                .append("]")
                .toString());
    }

    @Override
    public String toString() {
        return new StringBuilder("Course [title=")
                .append(title)
                .append(", instructor=").append(instructorName)
                .append(", duration=").append(durationHours)
                .append("]")
                .toString();
    }

    public String getTitle() { return title; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
    public int getDurationHours() { return durationHours; }
}
