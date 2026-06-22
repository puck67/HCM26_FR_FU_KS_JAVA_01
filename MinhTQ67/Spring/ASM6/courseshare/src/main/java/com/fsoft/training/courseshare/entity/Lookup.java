package com.fsoft.training.courseshare.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "tbl_lookup")
public class Lookup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "lookup_type", nullable = false)
    private String lookupType; // e.g., "COURSE_STATUS", "REVIEW_STATUS"

    @Column(name = "lookup_code", nullable = false)
    private Integer lookupCode; // e.g., 1

    @Column(name = "lookup_value", nullable = false)
    private String lookupValue; // e.g., "Draft"

    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getLookupType() { return lookupType; }
    public void setLookupType(String lookupType) { this.lookupType = lookupType; }
    public Integer getLookupCode() { return lookupCode; }
    public void setLookupCode(Integer lookupCode) { this.lookupCode = lookupCode; }
    public String getLookupValue() { return lookupValue; }
    public void setLookupValue(String lookupValue) { this.lookupValue = lookupValue; }
}
