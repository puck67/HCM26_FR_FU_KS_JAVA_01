package com.example.demo.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "courses")
public class Course {

    @EmbeddedId
    private CourseId id;
    
    @Column(name = "course_name", nullable = false)
    private String courseName;


    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String instructor;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Lesson> lessons = new ArrayList<>();

    public Course() {}

    public Course(CourseId id, String courseName, String category, String instructor) {
        this.id = id;
        this.courseName = courseName;
        this.category = category;
        this.instructor = instructor;
    }

    public CourseId getId() { return id; }
    public void setId(CourseId id) { this.id = id; }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getInstructor() { return instructor; }
    public void setInstructor(String instructor) { this.instructor = instructor; }

    public List<Lesson> getLessons() { return lessons; }
    public void setLessons(List<Lesson> lessons) { this.lessons = lessons; }

    public String getCourseCode() {
        return id != null ? id.getCourseCode() : null;
    }

    public LocalDate getStartDate() {
        return id != null ? id.getStartDate() : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(id, course.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
