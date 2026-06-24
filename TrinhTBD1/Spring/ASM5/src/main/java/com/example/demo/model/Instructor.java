package com.example.demo.model;

import com.example.demo.util.ValidationUtils;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Instructor extends LmsUser {
    private static final long serialVersionUID = 1L;

    @NotBlank(message = "cannot be null or empty")
    private String department;

    @NotBlank(message = "cannot be null or empty")
    private String bio;

    public Instructor(long userId, String name, String email, String department, String bio) {
        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.println(new StringBuilder("Instructor [ID=")
                .append(userId)
                .append(", Name=")
                .append(name)
                .append(", Email=")
                .append(email)
                .append(", Dept=")
                .append(department)
                .append(", Bio=")
                .append(bio)
                .append("]")
                .toString());
    }
}
