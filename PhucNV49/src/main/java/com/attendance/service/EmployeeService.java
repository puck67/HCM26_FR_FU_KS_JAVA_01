package com.attendance.service;

import com.attendance.model.Employee;
import com.attendance.repository.EmployeeRepository;
import com.attendance.repository.EmployeeRepositoryImpl;

import java.util.List;

public class EmployeeService {
    private final EmployeeRepository repository;

    public EmployeeService() {
        this.repository = new EmployeeRepositoryImpl();
    }

    public EmployeeService(EmployeeRepository repository) {
        this.repository = repository;
    }

    public void addEmployee(String id, String name, String department, String position) {
        Employee emp = new Employee(id, name, department, position);
        repository.save(emp);
        System.out.println("Employee added: " + emp);
    }

    public Employee getEmployee(String id) {
        return repository.findById(id);
    }

    public List<Employee> getAllEmployees() {
        return repository.findAll();
    }

    public void updateEmployee(String id, String name, String department, String position) {
        Employee emp = new Employee(id, name, department, position);
        repository.update(emp);
        System.out.println("Employee updated: " + emp);
    }

    public void deleteEmployee(String id) {
        repository.delete(id);
        System.out.println("Employee deleted: " + id);
    }
}
