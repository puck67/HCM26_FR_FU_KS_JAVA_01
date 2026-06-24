package fa.training.model;

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

    public String getTitle() { return title; }
    public String getInstructorName() { return instructorName; }
    public String getDescription() { return description; }
    public int getDurationHours() { return durationHours; }

    public void setTitle(String title) { this.title = title; }
    public void setInstructorName(String instructorName) { this.instructorName = instructorName; }
    public void setDescription(String description) { this.description = description; }
    public void setDurationHours(int durationHours) { this.durationHours = durationHours; }

    public void displayInfo() {
        System.out.println("[Course: " + title + "] by " + instructorName
                + " (" + durationHours + " hours): " + description);
    }
}
