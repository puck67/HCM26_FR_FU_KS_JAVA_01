package com.attendance.repository;

import com.attendance.model.Employee;

import java.util.List;

public interface EmployeeRepository {
    void save(Employee employee);
    Employee findById(String id);
    List<Employee> findAll();
    void update(Employee employee);
    void delete(String id);
}
