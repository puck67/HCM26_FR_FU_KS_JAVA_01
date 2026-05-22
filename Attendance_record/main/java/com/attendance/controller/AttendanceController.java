package com.attendance.controller;

import com.attendance.model.AttendanceRecord;
import com.attendance.service.AttendanceService;

import java.time.LocalDate;
import java.util.List;

public class AttendanceController {
    private final AttendanceService service;

    public AttendanceController(AttendanceService service) {
        this.service = service;
    }

    public void checkIn(String employeeId, String employeeName) {
        service.checkIn(employeeId, employeeName);
    }

    public void checkOut(int recordId) {
        service.checkOut(recordId);
    }

    public void markAbsent(String employeeId, String employeeName, LocalDate date) {
        service.markAbsent(employeeId, employeeName, date);
    }

    public List<AttendanceRecord> getByDate(LocalDate date) {
        return service.getByDate(date);
    }

    public List<AttendanceRecord> getByEmployee(String employeeId) {
        return service.getByEmployee(employeeId);
    }

    public List<AttendanceRecord> getAll() {
        return service.getAll();
    }

    public void delete(int id) {
        service.deleteRecord(id);
    }
}
