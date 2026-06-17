package com.lms.model;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_category")
public class Category {

    @Id
    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private Integer frequency;

    public Category() {
        this.frequency = 0;
    }

    public Category(String name, Integer frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getFrequency() {
        return frequency;
    }

    public void setFrequency(Integer frequency) {
        this.frequency = frequency;
    }
}
