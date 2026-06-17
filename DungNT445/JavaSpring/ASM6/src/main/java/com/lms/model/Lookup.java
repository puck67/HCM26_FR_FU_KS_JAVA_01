package com.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_lookup")
public class Lookup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type; // e.g. "course_status", "review_status"

    @Column(nullable = false)
    private Integer code;

    @Column(nullable = false)
    private String label; // e.g. "Draft", "Published", "Archived", "Pending", "Approved"

    public Lookup() {}

    public Lookup(String type, Integer code, String label) {
        this.type = type;
        this.code = code;
        this.label = label;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
