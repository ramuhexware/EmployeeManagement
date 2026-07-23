package com.example.employeemanagement.validator;

import com.example.employeemanagement.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeValidator {

    public void validateNewEmployee(Employee employee) {
        if (employee.getName() == null || employee.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Employee name cannot be empty");
        }
        if (employee.getEmail() == null || !employee.getEmail().contains("@")) {
            throw new IllegalArgumentException("Invalid employee email address");
        }
        if (employee.getSalary() <= 0) {
            throw new IllegalArgumentException("Employee salary must be positive");
        }
    }

    public void validatePromotion(Employee employee, double newSalary, String newRole) {
        if (!"ACTIVE".equalsIgnoreCase(employee.getStatus())) {
            throw new IllegalStateException("Only active employees can be promoted");
        }
        if (newSalary <= employee.getSalary()) {
            throw new IllegalArgumentException("New salary must be greater than current salary (" + employee.getSalary() + ")");
        }
        if (newRole == null || newRole.trim().isEmpty()) {
            throw new IllegalArgumentException("New role cannot be empty");
        }
    }
}
