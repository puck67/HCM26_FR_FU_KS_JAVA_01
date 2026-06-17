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
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    private long studentId;
    private String name;
    private double gpa;

    public void printInfo() {
        System.out.println("Student [ID=" + studentId + ", Name=" + name + ", GPA=" + gpa + "]");
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", name=" + name + ", gpa=" + gpa + "]";
    }
}