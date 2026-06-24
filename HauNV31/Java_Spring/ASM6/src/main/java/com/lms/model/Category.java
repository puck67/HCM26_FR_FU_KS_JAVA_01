package com.lms.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tbl_category")
public class Category {
    @Id
    private String name;
    private Integer frequency;

    public Category() {}

    public Category(String name, Integer frequency) {
        this.name = name;
        this.frequency = frequency;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Integer getFrequency() { return frequency; }
    public void setFrequency(Integer frequency) { this.frequency = frequency; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Category{name='").append(name).append('\'')
          .append(", frequency=").append(frequency)
          .append('}');
        return sb.toString();
    }
}
