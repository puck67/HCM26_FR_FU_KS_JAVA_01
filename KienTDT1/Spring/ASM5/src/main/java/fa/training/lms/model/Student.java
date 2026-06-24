package fa.training.lms.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "student")
public class Student implements Serializable {

    @Id
    private Long studentId;
    private String name;
    private double gpa;

    public Student() {
    }

    public Student(Long studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    public Long getStudentId() {
        return studentId;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    public void printInfo() {
        System.out.println(
                "Student [ID=" + studentId
                        + ", Name=" + name
                        + ", GPA=" + gpa + "]");
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId
                + ", name=" + name
                + ", gpa=" + gpa + "]";
    }
}