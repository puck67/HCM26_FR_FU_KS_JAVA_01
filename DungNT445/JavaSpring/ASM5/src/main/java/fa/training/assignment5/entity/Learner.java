package fa.training.assignment5.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@Table(name = "learners")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Learner {

    private static final Logger log = LoggerFactory.getLogger(Learner.class);

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String learnerName;
    private double gradePointAverage;

    public Learner(String learnerName, double gradePointAverage) {
        this.learnerName = learnerName;
        this.gradePointAverage = gradePointAverage;
    }

    public void showInfo() {
        log.info("Learner [ID={}, Name={}, GPA={}]", studentId, learnerName, gradePointAverage);
    }
}
