package com.attendance.repository;

import com.attendance.model.AttendanceRecord;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceRepository {
    void save(AttendanceRecord record);
    AttendanceRecord findById(int id);
    List<AttendanceRecord> findAll();
    List<AttendanceRecord> findByEmployeeId(String employeeId);
    List<AttendanceRecord> findByDate(LocalDate date);
    void update(AttendanceRecord record);
    void delete(int id);
}
