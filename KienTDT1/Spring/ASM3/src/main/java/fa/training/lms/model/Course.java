package fa.training.lms.model;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.io.Serializable;

@Entity
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String instructorName;
    private String description;
    private int durationHours;

    public Course() {
        // default constructor for JPA
    }

    public Course(String title, String instructorName,
                  String description, int durationHours) {
        this.title = title;
        this.instructorName = instructorName;
        this.description = description;
        this.durationHours = durationHours;
    }

    // getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void displayInfo() {
        System.out.println("[Course: " + title + "] by "
                + instructorName + " (" + durationHours
                + " hours): " + description);
    }

    @Override
    public String toString() {
        return "Course [id=" + id + ", title=" + title
                + ", instructor=" + instructorName
                + ", duration=" + durationHours + "]";
    }
}