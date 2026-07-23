package com.example.employeemanagement.controller;

import com.example.employeemanagement.model.Employee;
import com.example.employeemanagement.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    // Flow 1: Hire Employee
    @PostMapping
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee created = employeeService.createEmployee(employee);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    // Flow 2: Retrieve Employee Details
    @GetMapping("/{id}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Integer id) {
        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // Flow 3: Promote Employee
    @PutMapping("/{id}/promote")
    public ResponseEntity<Employee> promoteEmployee(
            @PathVariable Integer id,
            @RequestParam double newSalary,
            @RequestParam String newRole) {
        Employee promoted = employeeService.promoteEmployee(id, newSalary, newRole);
        return ResponseEntity.ok(promoted);
    }

    // Flow 4: Deactivate Employee
    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> deactivateEmployee(@PathVariable Integer id) {
        Employee deactivated = employeeService.deactivateEmployee(id);
        return ResponseEntity.ok(deactivated);
    }

    // Flow 5: Get Active Employees by Department
    @GetMapping("/department/{department}")
    public ResponseEntity<List<Employee>> getActiveEmployeesByDept(@PathVariable String department) {
        List<Employee> employees = employeeService.getEmployeesByDepartmentAndStatus(department, "ACTIVE");
        return ResponseEntity.ok(employees);
    }
}
