package com.attendance.model;

import java.time.LocalDate;
import java.time.LocalTime;

public class AttendanceRecord {
    private int id;
    private String employeeId;
    private String employeeName;
    private LocalDate date;
    private LocalTime checkIn;
    private LocalTime checkOut;
    private String status; 

    public AttendanceRecord() {}

    public AttendanceRecord(int id, String employeeId, String employeeName,
                            LocalDate date, LocalTime checkIn, LocalTime checkOut, String status) {
        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.date = date;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.status = status;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getEmployeeId() { return employeeId; }
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }

    public String getEmployeeName() { return employeeName; }
    public void setEmployeeName(String employeeName) { this.employeeName = employeeName; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public LocalTime getCheckIn() { return checkIn; }
    public void setCheckIn(LocalTime checkIn) { this.checkIn = checkIn; }

    public LocalTime getCheckOut() { return checkOut; }
    public void setCheckOut(LocalTime checkOut) { this.checkOut = checkOut; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    @Override
    public String toString() {
        return String.format("[%d] %s (%s) | %s | In: %s | Out: %s | %s",
                id, employeeName, employeeId, date, checkIn, checkOut, status);
    }
}
