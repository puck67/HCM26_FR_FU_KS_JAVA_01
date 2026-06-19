package fa.training.lms.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("INSTRUCTOR")
public class Instructor extends LmsUser {

    private String department;
    private String bio;

    public Instructor() {
    }

    public Instructor(Long userId,
                      String name,
                      String email,
                      String department,
                      String bio) {

        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.println(
                "Instructor [ID=" + getUserId()
                        + ", Name=" + getName()
                        + ", Email=" + getEmail()
                        + ", Dept=" + department
                        + ", Bio=" + bio + "]");
    }
}