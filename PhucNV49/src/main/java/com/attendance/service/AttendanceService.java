package com.attendance.service;

import com.attendance.model.AttendanceRecord;
import com.attendance.repository.AttendanceRepository;
import com.attendance.repository.AttendanceRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class AttendanceService {
    private final AttendanceRepository repository;

    public AttendanceService() {
        this.repository = new AttendanceRepositoryImpl();
    }

    public AttendanceService(AttendanceRepository repository) {
        this.repository = repository;
    }

    public void checkIn(String employeeId, String employeeName) {
        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeId(employeeId);
        record.setEmployeeName(employeeName);
        record.setDate(LocalDate.now());
        record.setCheckIn(LocalTime.now());

        LocalTime lateThreshold = LocalTime.of(9, 0);
        record.setStatus(record.getCheckIn().isAfter(lateThreshold) ? "LATE" : "PRESENT");

        repository.save(record);
        System.out.println("Check-in recorded: " + record);
    }

    public void checkOut(int recordId) {
        AttendanceRecord record = repository.findById(recordId);
        if (record == null) {
            System.out.println("Record not found: " + recordId);
            return;
        }
        record.setCheckOut(LocalTime.now());
        repository.update(record);
        System.out.println("Check-out recorded: " + record);
    }

    public void markAbsent(String employeeId, String employeeName, LocalDate date) {
        AttendanceRecord record = new AttendanceRecord();
        record.setEmployeeId(employeeId);
        record.setEmployeeName(employeeName);
        record.setDate(date);
        record.setStatus("ABSENT");
        repository.save(record);
    }

    public List<AttendanceRecord> getByEmployee(String employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public List<AttendanceRecord> getByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<AttendanceRecord> getAll() {
        return repository.findAll();
    }

    public void deleteRecord(int id) {
        repository.delete(id);
    }
}
