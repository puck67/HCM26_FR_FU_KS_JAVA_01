package com.example.demo.model;

import com.example.demo.util.ValidationUtils;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Student implements Serializable {
    private static final long serialVersionUID = 1L;

    @Positive(message = "must be a positive number")
    private long studentId;

    @NotBlank(message = "cannot be null or empty")
    private String name;

    @DecimalMin(value = "0.0", message = "must be between 0.0 and 4.0")
    @DecimalMax(value = "4.0", message = "must be between 0.0 and 4.0")
    private double gpa;

    public Student(long studentId, String name, double gpa) {
        this.studentId = studentId;
        this.name = name;
        this.gpa = gpa;
    }

    public void printInfo() {
        System.out.println(new StringBuilder("Student [ID=")
                .append(studentId)
                .append(", Name=")
                .append(name)
                .append(", GPA=")
                .append(gpa)
                .append("]")
                .toString());
    }

    @Override
    public String toString() {
        return "Student [studentId=" + studentId + ", name=" + name + ", gpa=" + gpa + "]";
    }
}
