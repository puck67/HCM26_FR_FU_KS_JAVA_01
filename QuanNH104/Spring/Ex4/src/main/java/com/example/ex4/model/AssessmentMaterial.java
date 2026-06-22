package com.example.ex4.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "assessment_materials")
public class AssessmentMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private String originalName;

    @Column(nullable = false)
    private String storedName;

    @Column(nullable = false)
    private Long size;

    @Column(nullable = false)
    private LocalDateTime uploadDate;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false)
    private String category; // Lecture, Assignment, Reference, Source Code

    // Constructors
    public AssessmentMaterial() {
    }

    public AssessmentMaterial(Long id, String title, String description, String originalName, String storedName, Long size, LocalDateTime uploadDate, String subject, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.originalName = originalName;
        this.storedName = storedName;
        this.size = size;
        this.uploadDate = uploadDate;
        this.subject = subject;
        this.category = category;
    }

    public AssessmentMaterial(String title, String description, String originalName, String storedName, Long size, LocalDateTime uploadDate, String subject, String category) {
        this.title = title;
        this.description = description;
        this.originalName = originalName;
        this.storedName = storedName;
        this.size = size;
        this.uploadDate = uploadDate;
        this.subject = subject;
        this.category = category;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOriginalName() {
        return originalName;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public String getStoredName() {
        return storedName;
    }

    public void setStoredName(String storedName) {
        this.storedName = storedName;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDateTime getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDateTime uploadDate) {
        this.uploadDate = uploadDate;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
