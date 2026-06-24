package com.example.ASM5.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class Instructor extends LmsUser {

    private static final long serialVersionUID = 2L;

    private String department;
    private String bio;

    public Instructor(long userId, String name, String email,
                      String department, String bio) {
        super(userId, name, email);
        this.department = department;
        this.bio = bio;
    }

    @Override
    public void printInfo() {
        System.out.println("Instructor [ID=" + userId
                + ", Name=" + name
                + ", Email=" + email
                + ", Dept=" + department
                + ", Bio=" + bio + "]");
    }
}