package fa.training.jsfw_m_a103.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Instructor extends LmsUser {
    private static final long serialVersionUID = 1L;

    private String department;
    private String bio;

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.println("Instructor [ID=" + getUserId() +
                ", Name=" + getName() +
                ", Email=" + getEmail() +
                ", Dept=" + department +
                ", Bio=" + bio + "]");
    }
}