package com.example.demo.model;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
public class Course {
    @EmbeddedId
    private CourseId id = new CourseId();

    private String courseName;

    private String category;

    private String instructor;

    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons = new ArrayList<>();

    public CourseId getId() {
        return id;
    }

    public void setId(CourseId id) {
        this.id = id;
    }

    public String getCourseCode() {
        return id.getCourseCode();
    }

    public void setCourseCode(String courseCode) {
        id.setCourseCode(courseCode);
    }

    public LocalDate getStartDate() {
        return id.getStartDate();
    }

    public void setStartDate(LocalDate startDate) {
        id.setStartDate(startDate);
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
