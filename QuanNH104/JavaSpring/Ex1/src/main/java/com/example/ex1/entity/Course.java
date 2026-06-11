package com.example.ex1.entity;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @EmbeddedId
    private CourseId id;

    @Column(name = "course_name", nullable = false)
    private String courseName;

    @Column(name = "category")
    private String category;

    @Column(name = "instructor")
    private String instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lesson> lessons;

    // Constructors
    public Course() {
    }

    public Course(CourseId id, String courseName, String category, String instructor, List<Lesson> lessons) {
        this.id = id;
        this.courseName = courseName;
        this.category = category;
        this.instructor = instructor;
        this.lessons = lessons;
    }

    // Getters and Setters
    public CourseId getId() {
        return id;
    }

    public void setId(CourseId id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
