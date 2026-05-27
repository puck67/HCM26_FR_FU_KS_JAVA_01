package org.fsa_2026.enities;

import java.util.Date;

public class Cousrse {
    private int id;
    private String cousrseName;
    private String description;
    private Date startTime;
    private Date endTime;

    public Cousrse() {
    }
    public Cousrse(int id, String cousrseName, String description, Date startTime, Date endTime) {
        this.id = id;
        this.cousrseName = cousrseName;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    //Getters and Setters
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getCousrseName() {
        return cousrseName;
    }
    public void setCousrseName(String cousrseName) {
        this.cousrseName = cousrseName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public Date getStartTime() {
        return startTime;
    }
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }
    public Date getEndTime() {
        return endTime;
    }
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
    public String toString() {
        return id + " " + cousrseName + " " + description + " " + startTime + " " + endTime;
    }

}
