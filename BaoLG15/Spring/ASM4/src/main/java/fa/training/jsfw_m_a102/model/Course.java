package fa.training.jsfw_m_a102.model;

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

    public void displayInfo() {
        System.out.printf("[Course: %s] by %s (%d hours): %s%n",
                title, instructorName, durationHours, description);
    }
}
