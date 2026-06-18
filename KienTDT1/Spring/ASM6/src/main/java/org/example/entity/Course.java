package org.example.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "courses")
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Course name is required")
    private String name;
    
    @NotBlank(message = "Course code is required")
    @Column(unique = true)
    private String code;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotNull(message = "Credits are required")
    private Integer credits;
    
    public Course() {}
    
    public Course(String name, String code, String description, Integer credits) {
        this.name = name;
        this.code = code;
        this.description = description;
        this.credits = credits;
    }
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getCode() {
        return code;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getCredits() {
        return credits;
    }
    
    public void setCredits(Integer credits) {
        this.credits = credits;
    }
}
