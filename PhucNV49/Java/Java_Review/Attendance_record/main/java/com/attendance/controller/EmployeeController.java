package com.attendance.controller;

import com.attendance.model.Employee;
import com.attendance.service.EmployeeService;
import com.attendance.validation.InputValidator;

import java.util.List;

public class EmployeeController {
    private final EmployeeService service;

    public EmployeeController(EmployeeService service) {
        this.service = service;
    }

    public void add(String id, String name, String department, String position) {
        if (!InputValidator.isValidEmployeeId(id)) throw new IllegalArgumentException("Invalid employee ID. Use format EMP001.");
        if (!InputValidator.isValidName(name)) throw new IllegalArgumentException("Invalid employee name.");
        if (!InputValidator.isValidText(department)) throw new IllegalArgumentException("Invalid department.");
        if (!InputValidator.isValidText(position)) throw new IllegalArgumentException("Invalid position.");
        if (service.getEmployee(id) != null) throw new IllegalStateException("Employee already exists: " + id);
        service.addEmployee(id, name, department, position);
    }

    public Employee get(String id) {
        return service.getEmployee(id);
    }

    public List<Employee> getAll() {
        return service.getAllEmployees();
    }

    public void update(String id, String name, String department, String position) {
        if (!InputValidator.isValidEmployeeId(id)) throw new IllegalArgumentException("Invalid employee ID. Use format EMP001.");
        if (!InputValidator.isValidName(name)) throw new IllegalArgumentException("Invalid employee name.");
        if (!InputValidator.isValidText(department)) throw new IllegalArgumentException("Invalid department.");
        if (!InputValidator.isValidText(position)) throw new IllegalArgumentException("Invalid position.");
        if (service.getEmployee(id) == null) throw new IllegalStateException("Employee not found: " + id);
        service.updateEmployee(id, name, department, position);
    }

    public void delete(String id) {
        if (!InputValidator.isValidEmployeeId(id)) throw new IllegalArgumentException("Invalid employee ID. Use format EMP001.");
        if (service.getEmployee(id) == null) throw new IllegalStateException("Employee not found: " + id);
        service.deleteEmployee(id);
    }
}
