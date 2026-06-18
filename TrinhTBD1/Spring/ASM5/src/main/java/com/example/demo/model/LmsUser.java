package com.example.demo.model;

import com.example.demo.util.ValidationUtils;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class LmsUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @Positive(message = "must be a positive number")
    protected long userId;

    @NotBlank(message = "cannot be null or empty")
    protected String name;

    @NotBlank(message = "cannot be null or empty")
    @Email(message = "format is invalid")
    protected String email;

    public LmsUser(long userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email = email;
    }

    public void printInfo() {
        System.out.println(new StringBuilder("User [ID=")
                .append(userId)
                .append(", Name=")
                .append(name)
                .append(", Email=")
                .append(email)
                .append("]")
                .toString());
    }

    @Override
    public String toString() {
        return "LmsUser [userId=" + userId + ", name=" + name + ", email=" + email + "]";
    }
}
