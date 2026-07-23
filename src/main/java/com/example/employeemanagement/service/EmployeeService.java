package com.example.employeemanagement.service;

import com.example.employeemanagement.exception.EmployeeNotFoundException;
import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.validator.EmployeeValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeValidator employeeValidator;
    private final AuditLogService auditLogService;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, 
                           EmployeeValidator employeeValidator, 
                           AuditLogService auditLogService) {
        this.employeeRepository = employeeRepository;
        this.employeeValidator = employeeValidator;
        this.auditLogService = auditLogService;
    }

    @Transactional
    public Employee createEmployee(Employee employee) {
        employeeValidator.validateNewEmployee(employee);
        employee.setStatus("ACTIVE");
        
        // DB Call 1: save
        Employee savedEmployee = employeeRepository.save(employee);
        
        auditLogService.logCreation(savedEmployee.getId(), savedEmployee.getName(), savedEmployee.getDepartment());
        return savedEmployee;
    }

    public Employee getEmployeeById(Integer id) {
        // DB Call 2: findById
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException("Employee with ID " + id + " not found"));
    }

    @Transactional
    public Employee promoteEmployee(Integer id, double newSalary, String newRole) {
        Employee employee = getEmployeeById(id);
        
        employeeValidator.validatePromotion(employee, newSalary, newRole);
        
        String oldRole = employee.getRole();
        double oldSalary = employee.getSalary();
        
        employee.setSalary(newSalary);
        employee.setRole(newRole);
        
        // DB Call 1: save (updates existing row)
        Employee updatedEmployee = employeeRepository.save(employee);
        
        auditLogService.logPromotion(updatedEmployee.getId(), oldRole, newRole, oldSalary, newSalary);
        return updatedEmployee;
    }

    @Transactional
    public Employee deactivateEmployee(Integer id) {
        Employee employee = getEmployeeById(id);
        employee.setStatus("INACTIVE");
        
        // DB Call 1: save (updates status field)
        Employee deactivatedEmployee = employeeRepository.save(employee);
        
        auditLogService.logDeactivation(deactivatedEmployee.getId(), deactivatedEmployee.getName());
        return deactivatedEmployee;
    }

    public List<Employee> getEmployeesByDepartmentAndStatus(String department, String status) {
        // DB Call 3: findByDepartmentAndStatus
        return employeeRepository.findByDepartmentAndStatus(department, status);
    }
}
