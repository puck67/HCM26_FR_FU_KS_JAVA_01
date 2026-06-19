package fa.training.lms.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "lms_user")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "user_type")
public class LmsUser implements Serializable {

    @Id
    private Long userId;

    private String name;

    private String email;

    public LmsUser() {
    }

    public LmsUser(Long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void printInfo() {
        System.out.println(
                "User [ID=" + userId +
                        ", Name=" + name +
                        ", Email=" + email + "]");
    }

    @Override
    public String toString() {
        return "LmsUser [userId=" + userId +
                ", name=" + name +
                ", email=" + email + "]";
    }
}