package fa.training.jsfw_m_a103.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;

    private long userId;
    private String name;
    private String email;

    public void printInfo() {
        System.out.println("User [ID=" + userId + ", Name=" + name + ", Email=" + email + "]");
    }

    @Override
    public String toString() {
        return "LmsUser [userId=" + userId + ", name=" + name + ", email=" + email + "]";
    }

}