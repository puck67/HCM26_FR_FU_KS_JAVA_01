package fa.training.assignment5.entity;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Entity
@DiscriminatorValue("INSTRUCTOR")
@Getter
@Setter
@NoArgsConstructor
public class SystemInstructor extends SystemUser {

    private static final Logger log = LoggerFactory.getLogger(SystemInstructor.class);

    private String faculty;
    private String biography;

    public SystemInstructor(String fullName, String emailAddress, String faculty, String biography) {
        super(fullName, emailAddress);
        this.faculty = faculty;
        this.biography = biography;
    }

    @Override
    public void displayDetails() {
        log.info("Instructor [ID={}, Name={}, Email={}, Faculty={}, Bio={}]",
                getId(), getFullName(), getEmailAddress(), faculty, biography);
    }
}
